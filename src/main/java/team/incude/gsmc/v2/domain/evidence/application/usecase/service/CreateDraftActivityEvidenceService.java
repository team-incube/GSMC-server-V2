package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.application.port.DraftActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incude.gsmc.v2.domain.evidence.application.usecase.CreateDraftActivityEvidenceUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.DraftActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.CreateDraftEvidenceResponse;
import team.incude.gsmc.v2.global.thirdparty.aws.exception.S3UploadFailedException;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateDraftActivityEvidenceService implements CreateDraftActivityEvidenceUseCase {

    private static final Long DRAFT_TTL_SECONDS = 7 * 24 * 60 * 60L; // 7일

    private final DraftActivityEvidencePersistencePort draftActivityEvidencePersistencePort;
    private final S3Port s3Port;

    @Override
    public CreateDraftEvidenceResponse execute(UUID id, String categoryName, String title, String content, MultipartFile file, String imageUrl, EvidenceType activityType) {
        UUID draftId = id == null ? UUID.randomUUID() : id;

        DraftActivityEvidence draftActivityEvidence = createActivityEvidence(draftId, categoryName, title, content, file, imageUrl, activityType);

        draftActivityEvidencePersistencePort.saveDraftActivityEvidence(draftActivityEvidence);
        return new CreateDraftEvidenceResponse(draftId);
    }

    private DraftActivityEvidence createActivityEvidence(UUID id, String categoryName, String title, String content, MultipartFile file, String imageUrlParam, EvidenceType evidenceType) {
        String imageUrl = null;

        if (file != null && !file.isEmpty()) {
            imageUrl = uploadFile(file);
        } else if (imageUrlParam != null && !imageUrlParam.isBlank()) {
            imageUrl = imageUrlParam;
        }

        return DraftActivityEvidence.builder()
                .id(id)
                .categoryName(categoryName)
                .title(title)
                .content(content)
                .imageUrl(imageUrl)
                .evidenceType(evidenceType)
                .ttl(DRAFT_TTL_SECONDS)
                .build();
    }

    private String uploadFile(MultipartFile file) {
        try {
            return s3Port.uploadFile(
                    file.getOriginalFilename(),
                    file.getInputStream()
            ).join();
        } catch (IOException e) {
            throw new S3UploadFailedException();
        }
    }
}
