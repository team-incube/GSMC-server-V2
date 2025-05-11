package team.incude.gsmc.v2.domain.evidence.presentation.data.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.global.annotation.validator.NotEmptyFile;

public record CreateOtherEvidenceRequest(
        @NotNull String categoryName,
        @NotEmptyFile MultipartFile file
) {
}
