package team.incude.gsmc.v2.domain.evidence.application.usecase;

import org.springframework.web.multipart.MultipartFile;

public interface CreateOtherEvidenceUseCase {
    void execute(String categoryName, MultipartFile file);
}
