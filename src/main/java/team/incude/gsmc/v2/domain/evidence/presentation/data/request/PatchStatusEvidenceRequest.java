package team.incude.gsmc.v2.domain.evidence.presentation.data.request;

import jakarta.validation.constraints.NotNull;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;

public record PatchStatusEvidenceRequest(
        @NotNull ReviewStatus status
) {
}
