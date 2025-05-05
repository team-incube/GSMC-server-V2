package team.incude.gsmc.v2.domain.score.presentation.data.request;

import jakarta.validation.constraints.NotNull;

/**
 * 점수 수정 요청을 위한 DTO입니다.
 * <p>카테고리 이름과 수정할 점수 값을 담아 클라이언트가 점수를 조정할 때 사용됩니다.
 * <ul>
 *   <li>{@code categoryName} - 점수를 수정할 카테고리 이름</li>
 *   <li>{@code value} - 적용할 점수 값</li>
 * </ul>
 * 전송되는 모든 필드는 필수이며 {@code @NotNull}로 검증됩니다.
 * @author snowykte0426
 */
public record PatchScoreRequest(
        @NotNull String categoryName,
        @NotNull Integer value
) {
}