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
import team.incude.gsmc.v2.global.thirdparty.aws.exception.S3UploadFailedException;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UpdateActivityEvidenceByCurrentUserService implements UpdateActivityEvidenceByCurrentUserUseCase {

    private final EvidencePersistencePort evidencePersistencePort;
    private final S3Port s3Port;
    private final ActivityEvidencePersistencePort activityEvidencePersistencePort;

    @Override
    @Transactional
    public void execute(Long evidenceId, String title, String content, MultipartFile file, EvidenceType evidenceType, String imageUrl) {
        Evidence evidence = evidencePersistencePort.findEvidenceByIdWithLock(evidenceId);
        ActivityEvidence activityEvidence = activityEvidencePersistencePort.findActivityEvidenceById(evidenceId);

        String fileUrl = checkImageUrl(activityEvidence, imageUrl, file);

        Evidence newEvidence = createEvidence(evidence, evidenceType);
        ActivityEvidence newActivityEvidence = createActivityEvidence(newEvidence, title, content, fileUrl);

        activityEvidencePersistencePort.saveActivityEvidence(newActivityEvidence);
    }

    private String checkImageUrl(ActivityEvidence activityEvidence, String imageUrl, MultipartFile file) {
        if (imageUrl != null
                && !imageUrl.isEmpty()
                && activityEvidence.getImageUrl().equals(imageUrl)) {
            return imageUrl;
        }

        s3Port.deleteFile(activityEvidence.getImageUrl());

        if (file != null && !file.isEmpty()){
            return uploadFile(file);
        } else {
            return null;
        }
    }

    private ActivityEvidence createActivityEvidence(Evidence evidence, String title, String content, String fileUrl) {
        return ActivityEvidence.builder()
                .id(evidence)
                .title(title)
                .content(content)
                .imageUrl(fileUrl)
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
