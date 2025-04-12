package team.incude.gsmc.v2.domain.evidence.presentation.data.response;

import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;

public record GetReadingEvidenceDto(
        Long id,
        String title,
        String author,
        Integer page,
        String content,
        ReviewStatus status
) {
}
