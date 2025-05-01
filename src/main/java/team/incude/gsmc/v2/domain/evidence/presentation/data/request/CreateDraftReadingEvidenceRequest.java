package team.incude.gsmc.v2.domain.evidence.presentation.data.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateDraftReadingEvidenceRequest(
        UUID draftId,
        @NotNull String title,
        @NotNull String author,
        @NotNull Integer page,
        @NotNull String content
) {
}
