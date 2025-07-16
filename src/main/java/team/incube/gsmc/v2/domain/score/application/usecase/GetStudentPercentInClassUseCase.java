package team.incube.gsmc.v2.domain.score.application.usecase;

import team.incube.gsmc.v2.domain.score.domain.constant.PercentileType;

/**
 * 학급 내 학생의 백분위(Percentile)를 조회하는 유즈케이스 인터페이스입니다.
 *
 * <p>특정 학년과 반에 속한 학생들의 점수를 기준으로 백분위 값을 계산하여,
 * 지정한 정렬 방식({@link PercentileType})에 따라 정렬된 상태에서 해당 학생의 위치를 파악합니다.</p>
 *
 * @author jihoonwjj
 */
public interface GetStudentPercentInClassUseCase {
    /**
     * 지정된 학년, 반, 정렬 기준에 따라 학생의 백분위 값을 계산합니다.
     *
     * @param percentileType 점수 정렬 기준 (예: 총점순, 국영수 평균순 등)
     * @param grade 대상 학년
     * @param classNumber 대상 반
     * @return 해당 정렬 기준에서의 학생 백분위 (0~100 범위의 정수값)
     *         반환값이 {@code null}일 수 있으므로 주의가 필요합니다.
     */
    Integer execute(PercentileType percentileType, Integer grade, Integer classNumber);
}
