package team.incude.gsmc.v2.domain.evidence.presentation.data.request;

import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record PatchActivityEvidenceRequest(
        @Size(max = 100) String title,
        @Size(max = 1500) String content,
        MultipartFile file,
        String imageUrl
) {
}
