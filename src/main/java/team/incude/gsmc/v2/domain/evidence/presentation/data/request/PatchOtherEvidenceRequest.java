package team.incude.gsmc.v2.domain.evidence.presentation.data.request;

import org.springframework.web.multipart.MultipartFile;

public record PatchOtherEvidenceRequest(
        MultipartFile file
) {
}
