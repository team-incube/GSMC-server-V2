package team.incube.gsmc.v2.domain.score.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incube.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incube.gsmc.v2.domain.score.application.usecase.GetStudentPercentInClassUseCase;
import team.incube.gsmc.v2.domain.score.domain.constant.PercentileType;
import team.incube.gsmc.v2.domain.score.exception.StudentClassMismatchException;
import team.incube.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;

/**
 * 학생의 성적 백분위수를 조회하는 서비스 클래스입니다.
 * 현재 로그인된 사용자의 이메일을 기반으로 특정 학년과 반에서의 성적 백분위수를 계산합니다.
 * @author jihoonwjj, snowykte0426
 */
@Service
@RequiredArgsConstructor
public class GetStudentPercentInClassService implements GetStudentPercentInClassUseCase {

    private final ScorePersistencePort scorePersistencePort;
    private final CurrentMemberProvider currentMemberProvider;

    /**
     * 현재 로그인된 학생의 백분위수를 조회합니다.
     *
     * @param percentileType 백분위 유형 (상위 또는 하위)
     * @param grade 조회 대상 학년
     * @param classNumber 조회 대상 반 번호
     * @return 계산된 백분위수
     * @throws StudentClassMismatchException 학년 또는 반 정보 요청한 사용자의 정보와 일치하지 않아 허용되지 않은 경우 예외 발생
     */
    public Integer execute(PercentileType percentileType, Integer grade, Integer classNumber) {
        String email = currentMemberProvider.getCurrentUser().getEmail();
        Integer studentScore = switch(percentileType) {
            case HIGH -> scorePersistencePort.getStudentHighPercentileByEmailInClass(email, grade, classNumber);
            case LOW -> scorePersistencePort.getStudentLowPercentileByEmailInClass(email, grade, classNumber);
        };
        
        if (studentScore == null) {
            throw new StudentClassMismatchException();
        }

        Long lowerCount = scorePersistencePort.countStudentsWithLowerScoreInClass(studentScore, grade, classNumber);
        Long totalCount = scorePersistencePort.countTotalStudentsInClass(grade, classNumber);

        return calculatePercentile(lowerCount, totalCount, percentileType);
    }
    
    private Integer calculatePercentile(Long lowerCount, Long totalCount, PercentileType percentileType) {
        if (totalCount == null || totalCount == 0) {
            return 0;
        }
        long lower = lowerCount != null ? lowerCount : 0;
        double percentile = switch(percentileType) {
            case HIGH -> ((double) (totalCount - lower) / totalCount) * 100;
            case LOW -> ((double) lower / totalCount) * 100;
        };
        return Math.toIntExact(Math.round(percentile));
    }
}
