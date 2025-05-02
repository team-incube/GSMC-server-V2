package team.incude.gsmc.v2.domain.evidence.presentation.data.response;

import java.util.UUID;

public record GetDraftReadingEvidenceResponse(
        UUID draftId,
        String title,
        String author,
        Integer page,
        String content
) {
}
