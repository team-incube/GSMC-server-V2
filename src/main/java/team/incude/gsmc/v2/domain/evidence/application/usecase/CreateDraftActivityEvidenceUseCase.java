package team.incude.gsmc.v2.domain.evidence.application.usecase;

import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.CreateDraftEvidenceResponse;

import java.util.UUID;

public interface CreateDraftActivityEvidenceUseCase {
    CreateDraftEvidenceResponse execute(UUID id, String categoryName, String title, String content, MultipartFile file, String imageUrl, EvidenceType activityType);
}
