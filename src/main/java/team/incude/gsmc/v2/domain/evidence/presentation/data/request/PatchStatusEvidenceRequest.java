package team.incude.gsmc.v2.domain.evidence.presentation.data.request;

import jakarta.validation.constraints.NotNull;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;

/**
 * 증빙자료의 상태(검토 상태)를 수정하기 위한 요청 DTO입니다.
 * <p>해당 증빙자료에 대해 검토 상태를 {@link ReviewStatus} 값으로 지정하여 변경할 수 있습니다.
 * @param status 새로운 검토 상태 (예: PENDING, APPROVED, REJECTED 등)
 * @author suuuuuuminnnnnn, snowykte0426
 */
public record PatchStatusEvidenceRequest(
        @NotNull ReviewStatus status
) {
}
