package team.incude.gsmc.v2.domain.evidence.presentation.data.response;

import java.util.List;

public record GetEvidencesResponse(
        List<GetActivityEvidenceResponse> majorActivityEvidence,
        List<GetActivityEvidenceResponse> humanitiesActivityEvidence,
        List<GetReadingEvidenceResponse> readingEvidence,
        List<GetOtherEvidenceResponse> otherEvidence
) {
}
