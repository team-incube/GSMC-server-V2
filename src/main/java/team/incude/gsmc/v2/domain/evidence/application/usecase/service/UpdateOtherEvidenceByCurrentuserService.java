package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.application.port.EvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incude.gsmc.v2.domain.evidence.application.usecase.UpdateOtherEvidenceByCurrentUserUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.global.thirdparty.aws.exception.S3UploadFailedException;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateOtherEvidenceByCurrentuserService implements UpdateOtherEvidenceByCurrentUserUseCase {

    private final OtherEvidencePersistencePort otherEvidencePersistencePort;
    private final EvidencePersistencePort evidencePersistencePort;
    private final S3Port s3Port;

    @Override
    @Transactional
    public void execute(Long evidenceId, MultipartFile file) {
        Evidence evidence = evidencePersistencePort.findEvidenceByIdWithLock(evidenceId);
        deleteFileByEvidenceId(evidenceId);

        Evidence newEvidence = createEvidence(evidence);
        String fileUrl = uploadFile(file);
        OtherEvidence newOtherEvidence = createOtherEvidence(newEvidence, fileUrl);

        otherEvidencePersistencePort.saveOtherEvidence(newOtherEvidence);
    }

    private void deleteFileByEvidenceId(Long evidenceId) {
        OtherEvidence otherEvidence = otherEvidencePersistencePort.findOtherEvidenceById(evidenceId);
        s3Port.deleteFile(otherEvidence.getFileUri());
    }

    private Evidence createEvidence(Evidence evidence) {
        return Evidence.builder()
                .id(evidence.getId())
                .score(evidence.getScore())
                .reviewStatus(ReviewStatus.PENDING)
                .evidenceType(evidence.getEvidenceType())
                .createdAt(evidence.getCreatedAt())
                .updatedAt(evidence.getUpdatedAt())
                .build();
    }

    private OtherEvidence createOtherEvidence(Evidence evidence, String fileUrl) {
        return OtherEvidence.builder()
                .id(evidence)
                .fileUri(fileUrl)
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
