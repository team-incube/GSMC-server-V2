package team.incude.gsmc.v2.domain.evidence.presentation.data.request;

import jakarta.validation.constraints.NotNull;

public record CreateReadingEvidenceRequest(
        @NotNull String title,
        @NotNull String author,
        @NotNull Integer page,
        @NotNull String content
) {
}
