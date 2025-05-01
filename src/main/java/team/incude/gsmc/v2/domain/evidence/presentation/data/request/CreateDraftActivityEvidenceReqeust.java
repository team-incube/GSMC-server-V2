package team.incude.gsmc.v2.domain.evidence.presentation.data.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;

import java.util.UUID;

public record CreateDraftActivityEvidenceReqeust(
        UUID draftId,
        @NotNull String categoryName,
        @NotNull String title,
        @NotNull String content,
        MultipartFile file,
        String imageUrl,
        @NotNull EvidenceType activityType
) {
}
