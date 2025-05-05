package team.incude.gsmc.v2.domain.score.application.usecase;

/**
 * 총합 점수 계산 유스케이스를 정의하는 인터페이스입니다.
 * <p>학생의 모든 카테고리 점수를 합산하여 총합 점수를 계산합니다.
 * 일반적으로 점수 변경 이후 자동으로 호출되어야 합니다.
 * @param studentCode 총점을 계산할 학생의 고유 코드
 * @author snowykte0426
 */
public interface CalculateTotalScoreUseCase {
    void execute(String studentCode);
}