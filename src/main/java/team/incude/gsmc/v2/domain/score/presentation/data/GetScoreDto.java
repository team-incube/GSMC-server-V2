package team.incude.gsmc.v2.domain.score.presentation.data;

/**
 * 개별 카테고리의 점수 정보를 나타내는 DTO입니다.
 *
 * <p>{@link team.incude.gsmc.v2.domain.score.presentation.data.response.GetScoreResponse}에서 각 카테고리별 점수를 전달할 때 사용되며,
 * 카테고리 이름과 해당 점수 값을 포함합니다.
 * <ul>
 *   <li>{@code categoryName} - 점수 카테고리 이름</li>
 *   <li>{@code value} - 해당 카테고리의 점수</li>
 * </ul>
 * 클라이언트는 이를 통해 상세 점수 분포를 확인할 수 있습니다.
 * @author snowykte0426
 */
public record GetScoreDto(
        String categoryName,
        Integer value
) {
}