package team.incube.gsmc.v2.domain.score.application.usecase;

import team.incube.gsmc.v2.domain.score.domain.constant.PercentileType;

/**
 * 학년 내 학생의 백분위(Percentile)를 조회하는 유즈케이스 인터페이스입니다.
 *
 * <p>특정 학년에 속한 모든 학생들을 대상으로 주어진 백분위 유형({@link PercentileType})에 따라
 * 정렬된 위치에서의 백분위 값을 계산합니다.</p>
 *
 * <p>예를 들어, 전체 점수 기준 또는 특정 과목 평균 기준으로 백분위를 계산할 수 있습니다.</p>
 *
 * @author jihoonwjj
 */
public interface GetStudentPercentInGradeUseCase {

    /**
     * 지정된 학년과 백분위 유형에 따라 학생의 백분위 값을 계산합니다.
     *
     * @param percentileType 백분위 계산 기준 (예: 총점, 국영수 평균 등)
     * @param grade 대상 학년
     * @return 해당 기준에서의 백분위 값 (0~100 범위의 정수값)
     */
    Integer execute(PercentileType percentileType, Integer grade);
}
