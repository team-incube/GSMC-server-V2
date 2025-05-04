package team.incude.gsmc.v2.domain.score.presentation.data.response;

/**
 * 모의 점수 계산 결과를 담는 응답 DTO입니다.
 * <p>입력된 시뮬레이션 요청 데이터를 바탕으로 계산된 총합 점수를 제공합니다.
 * <ul>
 *   <li>{@code totalScore} - 시뮬레이션에 의해 계산된 총 점수</li>
 * </ul>
 * 이 DTO는 사용자가 입력한 항목들에 대해 예상 점수를 확인할 때 사용됩니다.
 * @author snowykte0426
 */
public record GetScoreSimulateResponse(
        Integer totalScore
) {
}