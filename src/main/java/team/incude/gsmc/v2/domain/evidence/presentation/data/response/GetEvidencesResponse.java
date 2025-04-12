package team.incude.gsmc.v2.domain.evidence.presentation.data.response;

import team.incude.gsmc.v2.domain.evidence.presentation.data.GetActivityEvidenceDto;
import team.incude.gsmc.v2.domain.evidence.presentation.data.GetOtherEvidenceDto;
import team.incude.gsmc.v2.domain.evidence.presentation.data.GetReadingEvidenceDto;

import java.util.List;

public record GetEvidencesResponse(
        List<GetActivityEvidenceDto> majorActivityEvidence,
        List<GetActivityEvidenceDto> humanitiesActivityEvidence,
        List<GetReadingEvidenceDto> readingEvidence,
        List<GetOtherEvidenceDto> otherEvidence
) {
}
