package team.incude.gsmc.v2.domain.evidence.presentation.data.response;

import java.util.List;

public record GetDraftEvidenceResponse(
        List<GetDraftActivityEvidenceResponse> activityEvidences,
        List<GetDraftReadingEvidenceResponse> readingEvidences
) {
}
