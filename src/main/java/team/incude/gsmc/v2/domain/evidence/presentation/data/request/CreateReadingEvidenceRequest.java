package team.incude.gsmc.v2.domain.evidence.presentation.data.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateReadingEvidenceRequest(
        @NotNull @Size(max = 100) String title,
        @NotNull @Size(max = 100) String author,
        @NotNull Integer page,
        @NotNull @Size(max = 1500) String content,
        UUID draftId
) {
}
