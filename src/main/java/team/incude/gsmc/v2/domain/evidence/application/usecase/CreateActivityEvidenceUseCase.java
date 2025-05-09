package team.incude.gsmc.v2.domain.evidence.application.usecase;

import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;

import java.util.UUID;

public interface CreateActivityEvidenceUseCase {
    void execute(String categoryName, String title, String content, MultipartFile file, String imageUrl, EvidenceType activityType, UUID draftId);
}
