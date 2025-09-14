package team.incube.gsmc.v2.domain.category.presentation.data.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 카테고리 정보를 클라이언트에 전달하기 위한 응답 DTO입니다.
 * <p>카테고리의 ID, 이름, 최대 점수, 가중치, 증빙 필요 여부, 한국어 이름을 포함하여 반환됩니다.
 * <ul>
 *   <li>{@code id} - 카테고리 고유 식별자</li>
 *   <li>{@code name} - 카테고리 영문 이름</li>
 *   <li>{@code maximumValue} - 해당 카테고리의 최대 점수</li>
 *   <li>{@code weight} - 점수 계산 시 적용되는 가중치</li>
 *   <li>{@code isEvidenceRequired} - 증빙 자료 제출 필요 여부</li>
 *   <li>{@code koreanName} - 카테고리 한국어 이름</li>
 * </ul>
 * 이 DTO는 주로 {@code GET /api/v2/category/{id}} 엔드포인트에서 사용됩니다.
 * @author snowykte0426
 */
@Schema(description = "카테고리 정보 응답 DTO")
public record GetCategoryResponse(
        @Schema(description = "카테고리 고유 식별자", example = "1")
        Long id,
        
        @Schema(description = "카테고리 영문 이름", example = "ACADEMIC_ACTIVITY")
        String name,
        
        @Schema(description = "해당 카테고리의 최대 점수", example = "100")
        Integer maximumValue,
        
        @Schema(description = "점수 계산 시 적용되는 가중치", example = "1.5")
        Float weight,
        
        @Schema(description = "증빙 자료 제출 필요 여부", example = "true")
        Boolean isEvidenceRequired,
        
        @Schema(description = "카테고리 한국어 이름", example = "학술 활동")
        String koreanName
) {
}