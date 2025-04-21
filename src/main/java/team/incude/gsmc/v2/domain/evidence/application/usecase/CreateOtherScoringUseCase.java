package team.incude.gsmc.v2.domain.evidence.application.usecase;

import org.springframework.web.multipart.MultipartFile;

public interface CreateOtherScoringUseCase {
    void execute(String categoryName, MultipartFile file, int value);
}
