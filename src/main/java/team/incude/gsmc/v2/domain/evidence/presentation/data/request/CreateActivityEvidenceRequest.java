package team.incude.gsmc.v2.domain.evidence.presentation.data.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;

import java.util.UUID;

public record CreateActivityEvidenceRequest(
        @NotNull String categoryName,
        @NotNull @Size(max = 100) String title,
        @NotNull @Size(max = 1500) String content,
        MultipartFile file,
        @NotNull EvidenceType activityType,
        UUID draftId
) {
}
