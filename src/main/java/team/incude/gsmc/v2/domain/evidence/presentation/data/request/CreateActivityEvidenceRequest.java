package team.incude.gsmc.v2.domain.evidence.presentation.data.request;

import jakarta.validation.constraints.NotNull;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;

public record CreateActivityEvidenceRequest(
        @NotNull String categoryName,
        @NotNull String title,
        @NotNull String content,
        @NotNull EvidenceType activityType
) {
}
