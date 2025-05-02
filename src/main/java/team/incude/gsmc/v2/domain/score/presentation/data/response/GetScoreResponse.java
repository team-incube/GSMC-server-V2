package team.incude.gsmc.v2.domain.score.presentation.data.response;

import team.incude.gsmc.v2.domain.score.presentation.data.GetScoreDto;

import java.util.List;

/**
 * 점수 조회 결과를 반환하는 응답 DTO입니다.
 * <p>총합 점수와 각 세부 카테고리별 점수 정보를 포함합니다.
 * <ul>
 *   <li>{@code totalScore} - 모든 카테고리 점수의 합산값</li>
 *   <li>{@code scores} - 개별 카테고리 점수를 담은 {@link GetScoreDto} 리스트</li>
 * </ul>
 * 클라이언트는 이 응답을 통해 사용자 점수 현황을 확인할 수 있습니다.
 * @author snowykte0426
 */
public record GetScoreResponse(
        Integer totalScore,
        List<GetScoreDto> scores
) {
}