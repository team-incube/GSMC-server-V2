package team.incude.gsmc.v2.domain.evidence.presentation.data.request;

import jakarta.validation.constraints.NotNull;

public record CreateOtherScoringEvidenceRequest(
        @NotNull String categoryName,
        @NotNull Integer value
) {
}
