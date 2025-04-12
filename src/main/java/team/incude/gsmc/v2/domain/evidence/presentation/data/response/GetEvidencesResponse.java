package team.incude.gsmc.v2.domain.evidence.presentation.data.response;

import java.util.List;

public record GetEvidencesResponse(
        List<GetActivityEvidenceDto> majorActivityEvidence,
        List<GetActivityEvidenceDto> humanitiesActivityEvidence,
        List<GetReadingEvidenceDto> readingEvidence,
        List<GetOtherEvidenceDto> otherEvidence
) {
}
