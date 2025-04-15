package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.EvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incude.gsmc.v2.domain.evidence.application.usecase.UpdateActivityEvidenceByCurrentUserUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.evidence.exception.ActivityEvidenceNotFoundException;
import team.incude.gsmc.v2.global.thirdparty.aws.exception.S3UploadFailedException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateActivityEvidenceByCurrentUserService implements UpdateActivityEvidenceByCurrentUserUseCase {

    private final EvidencePersistencePort evidencePersistencePort;
    private final ActivityEvidencePersistencePort activityEvidencePersistencePort;
    private final S3Port s3Port;

    @Override
    @Transactional
    public void execute(Long evidenceId, String title, String content, MultipartFile file, EvidenceType evidenceType) {
        Evidence evidence = evidencePersistencePort.findEvidenceById(evidenceId);
        validateNotExistsActivity(evidenceId);

        Evidence newEvidence = createEvidence(evidence, evidenceType);
        ActivityEvidence newActivityEvidence = createActivityEvidence(newEvidence, title, content, file);
        saveActivityEvidence(newActivityEvidence);
    }

    private void validateNotExistsActivity(Long evidenceId) {
        if (activityEvidencePersistencePort.existsActivityEvidenceByEvidenceId(evidenceId)) {
            throw new ActivityEvidenceNotFoundException();
        }
    }

    private ActivityEvidence createActivityEvidence(Evidence evidence, String title, String content, MultipartFile file) {
        return ActivityEvidence.builder()
                .id(evidence)
                .title(title)
                .content(content)
                .imageUrl(uploadFile(file))
                .build();
    }

    private Evidence createEvidence(Evidence evidence, EvidenceType evidenceType) {
        return Evidence.builder()
                .id(evidence.getId())
                .score(evidence.getScore())
                .reviewStatus(ReviewStatus.PENDING)
                .evidenceType(evidenceType)
                .createdAt(evidence.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;

        try {
            return s3Port.uploadFile(
                    UUID.randomUUID().toString(),
                    file.getInputStream()
            ).join();
        } catch (IOException e) {
            throw new S3UploadFailedException();
        }
    }

    private void saveActivityEvidence(ActivityEvidence activityEvidence) {
        activityEvidencePersistencePort.saveActivityEvidence(activityEvidence);
    }
}
