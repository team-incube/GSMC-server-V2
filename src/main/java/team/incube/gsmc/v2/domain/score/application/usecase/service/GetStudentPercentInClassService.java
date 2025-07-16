package team.incube.gsmc.v2.domain.score.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incube.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incube.gsmc.v2.domain.score.application.usecase.GetStudentPercentInClassUseCase;
import team.incube.gsmc.v2.domain.score.domain.constant.PercentileType;
import team.incube.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;

/**
 * 학생의 성적 백분위를 조회하는 서비스 클래스입니다.
 * 현재 로그인된 사용자의 이메일을 기반으로 특정 학년과 반에서의 성적 백분위를 계산합니다.
 * @author jihoonwjj
 */
@Service
@RequiredArgsConstructor
public class GetStudentPercentInClassService implements GetStudentPercentInClassUseCase {

    private final ScorePersistencePort scorePersistencePort;
    private final CurrentMemberProvider currentMemberProvider;

    /**
     * 현재 로그인된 학생의 백분위를 조회합니다.
     *
     * @param percentileType 백분위 유형 (상위 또는 하위)
     * @param grade 조회 대상 학년
     * @param classNumber 조회 대상 반 번호
     * @return 계산된 백분위 (0~100 범위의 정수)
     * @throws team.incube.gsmc.v2.domain.score.exception.StudentClassMismatchException 학년 또는 반 정보가 허용되지 않은 경우 예외 발생 가능
     */
    public Integer execute(PercentileType percentileType, Integer grade, Integer classNumber) {

        return switch(percentileType) {
            case HIGH -> scorePersistencePort.getStudentHighPercentileByEmailInClass(currentMemberProvider.getCurrentUser().getEmail(), grade, classNumber);
            case LOW -> scorePersistencePort.getStudentLowPercentileByEmailInClass(currentMemberProvider.getCurrentUser().getEmail(), grade, classNumber);
        };
    }
}
