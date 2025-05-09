package team.incude.gsmc.v2.domain.evidence.presentation.data.response;

import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;

public record GetOtherEvidenceResponse(
        Long id,
        String fileUri,
        EvidenceType evidenceType,
        ReviewStatus status,
        String categoryName
) {
}
