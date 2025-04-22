package team.incude.gsmc.v2.domain.evidence.presentation.data.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;

public record CreateActivityEvidenceRequest(
        @NotNull String categoryName,
        @NotNull String title,
        @NotNull String content,
        MultipartFile file,
        @NotNull EvidenceType activityType
) {
}
