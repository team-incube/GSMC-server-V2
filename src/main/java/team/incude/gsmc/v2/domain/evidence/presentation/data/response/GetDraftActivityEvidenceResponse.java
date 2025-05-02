package team.incude.gsmc.v2.domain.evidence.presentation.data.response;

import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;

import java.util.UUID;

public record GetDraftActivityEvidenceResponse(
        UUID draftId,
        String categoryName,
        String title,
        String content,
        String imageUrl,
        EvidenceType activityType
) {
}
