package team.incude.gsmc.v2.domain.evidence.application.usecase;

import org.springframework.web.multipart.MultipartFile;

public interface CreateOtherScoringEvidenceUseCase {
    void execute(String categoryName, MultipartFile file, int value);
}
