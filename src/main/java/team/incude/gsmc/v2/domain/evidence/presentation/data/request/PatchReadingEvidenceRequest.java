package team.incude.gsmc.v2.domain.evidence.presentation.data.request;

import jakarta.validation.constraints.Size;

public record PatchReadingEvidenceRequest(
        @Size(max = 100) String title,
        @Size(max = 100) String author,
        @Size(max = 1500) String content,
        Integer page
) {
}
