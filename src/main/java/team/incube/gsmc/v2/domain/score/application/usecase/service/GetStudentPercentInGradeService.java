package team.incube.gsmc.v2.domain.score.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incube.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incube.gsmc.v2.domain.member.domain.StudentDetail;
import team.incube.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incube.gsmc.v2.domain.score.application.usecase.GetStudentPercentInGradeUseCase;
import team.incube.gsmc.v2.domain.score.domain.constant.PercentileType;
import team.incube.gsmc.v2.domain.score.exception.StudentClassMismatchException;
import team.incube.gsmc.v2.domain.score.presentation.data.response.GetStudentPercentResponse;
import team.incube.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;


/**
 * 학생의 성적 백분위를 학년 단위로 조회하는 서비스 클래스입니다.
 * 현재 로그인된 사용자의 이메일을 기반으로 특정 학년에서의 성적 백분위를 계산합니다.
 */
@Service
@RequiredArgsConstructor
public class GetStudentPercentInGradeService implements GetStudentPercentInGradeUseCase {

    private final ScorePersistencePort scorePersistencePort;
    private final StudentDetailPersistencePort studentDetailPersistencePort;
    private final CurrentMemberProvider currentMemberProvider;

    /**
     * 현재 로그인된 학생의 학년 내 백분위를 조회합니다.
     * @param percentileType 백분위 유형 (상위 또는 하위)
     * @param grade 조회 대상 학년
     * @return 계산된 백분위 (0~100 범위의 정수)
     * @throws team.incube.gsmc.v2.domain.score.exception.StudentClassMismatchException 학년 정보가 유효하지 않거나 허용되지 않은 경우 예외 발생 가능
     */

    public GetStudentPercentResponse execute(PercentileType percentileType, Integer grade) {
        String email = currentMemberProvider.getCurrentUser().getEmail();

        StudentDetail studentDetail = studentDetailPersistencePort.findStudentDetailByEmail(email);

        if (!(studentDetail.getGrade().equals(grade))) {
            throw new StudentClassMismatchException();
        }

        Integer studentScore = studentDetailPersistencePort.findTotalScoreByEmail(email);

        Long lowerCount = scorePersistencePort.countStudentsWithLowerScoreInGrade(studentScore, grade);

        Long totalCount = scorePersistencePort.countTotalStudentsInGrade(grade);

        double topPercentile = ((double) lowerCount / totalCount) * 100;

        double bottomPercentile = 100.0 - topPercentile;

        return new GetStudentPercentResponse(
                switch(percentileType) {
                    case HIGH -> Math.toIntExact(Math.round(topPercentile));
                    case LOW -> Math.toIntExact(Math.round(bottomPercentile));
                }
        );
    }
}