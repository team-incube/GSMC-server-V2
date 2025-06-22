package team.incube.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incube.gsmc.v2.domain.evidence.application.usecase.UpdateEvidenceFileUseCase;
import team.incube.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incube.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incube.gsmc.v2.domain.evidence.domain.constant.EvidenceType;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UpdateEvidenceFileService implements UpdateEvidenceFileUseCase {

    private final S3Port s3Port;
    private final ActivityEvidencePersistencePort activityEvidencePersistencePort;
    private final OtherEvidencePersistencePort otherEvidencePersistencePort;
    private final RetryTemplate retryTemplate;

    @Override
    public void execute(Long evidenceId, String fileName, InputStream inputStream, EvidenceType evidenceType) {
        String fileUrl = uploadFile(fileName, inputStream);

        switch (evidenceType) {
            case MAJOR, HUMANITIES -> {
                ActivityEvidence ae = activityEvidencePersistencePort.findActivityEvidenceById(evidenceId);

                activityEvidencePersistencePort.saveActivityEvidence(ae.getId(), updateActivityEvidence(ae, fileUrl));
            }
            default -> {
                OtherEvidence oe = otherEvidencePersistencePort.findOtherEvidenceById(evidenceId);

                otherEvidencePersistencePort.saveOtherEvidence(oe.getId(), updateOtherEvidence(oe, fileUrl));
            }
        }
    }

    private String uploadFile(String fileName, InputStream inputStream) {
        return retryTemplate.execute(context -> s3Port.uploadFile(fileName, inputStream).join());
    }

    private ActivityEvidence updateActivityEvidence(ActivityEvidence activityEvidence, String fileUrl) {
        return ActivityEvidence.builder()
                .id(activityEvidence.getId())
                .title(activityEvidence.getTitle())
                .imageUrl(fileUrl)
                .content(activityEvidence.getContent())
                .build();
    }

    private OtherEvidence updateOtherEvidence(OtherEvidence otherEvidence, String fileUrl) {
        return OtherEvidence.builder()
                .id(otherEvidence.getId())
                .fileUri(fileUrl)
                .build();
    }
}
