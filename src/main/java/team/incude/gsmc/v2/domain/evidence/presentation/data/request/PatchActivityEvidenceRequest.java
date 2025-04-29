package team.incude.gsmc.v2.domain.evidence.presentation.data.request;

import org.springframework.web.multipart.MultipartFile;

public record PatchActivityEvidenceRequest(
        String title,
        String content,
        MultipartFile file,
        String imageUrl
) {
}
