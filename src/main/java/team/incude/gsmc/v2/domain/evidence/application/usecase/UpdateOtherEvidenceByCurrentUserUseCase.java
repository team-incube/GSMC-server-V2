package team.incude.gsmc.v2.domain.evidence.application.usecase;

import org.springframework.web.multipart.MultipartFile;

public interface UpdateOtherEvidenceByCurrentUserUseCase {
    void execute(Long evidenceId, MultipartFile file, String imageUrl);
}
