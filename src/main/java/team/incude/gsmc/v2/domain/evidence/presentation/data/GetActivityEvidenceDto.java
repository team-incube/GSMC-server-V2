package team.incude.gsmc.v2.domain.evidence.presentation.data;

import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;

public record GetActivityEvidenceDto(
        Long id,
        String title,
        String content,
        String imageUri,
        ReviewStatus status,
        String categoryName
) {
}
