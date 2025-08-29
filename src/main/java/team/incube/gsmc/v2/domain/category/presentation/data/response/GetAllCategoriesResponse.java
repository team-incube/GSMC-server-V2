package team.incube.gsmc.v2.domain.category.presentation.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * 모든 카테고리 목록을 클라이언트에 전달하기 위한 응답 DTO입니다.
 * <p>시스템에 등록된 모든 점수 카테고리의 정보를 리스트 형태로 포함하며,
 * 각 카테고리는 {@link GetCategoryResponse} 형태로 구성됩니다.
 * <ul>
 *   <li>{@code categories} - 전체 카테고리 정보를 담은 리스트</li>
 * </ul>
 * 이 DTO는 주로 {@code GET /api/v2/category} 엔드포인트에서 사용되며,
 * 클라이언트에서 카테고리 선택 목록을 구성할 때 활용됩니다.
 * @author snowykte0426
 */
@Schema(description = "모든 카테고리 목록 응답 DTO")
public record GetAllCategoriesResponse(
        @Schema(description = "전체 카테고리 정보를 담은 리스트")
        List<GetCategoryResponse> categories
) {
}