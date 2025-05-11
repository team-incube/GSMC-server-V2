package team.incude.gsmc.v2.domain.evidence.presentation.data.request;

import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;

public record PatchStatusEvidenceRequest(
        ReviewStatus status
) {
}
