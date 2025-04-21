package team.incude.gsmc.v2.domain.evidence.presentation.data.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record CreateOtherScoringEvidenceRequest(
        @NotNull String categoryName,
        MultipartFile file,
        @NotNull Integer value
) {
}
