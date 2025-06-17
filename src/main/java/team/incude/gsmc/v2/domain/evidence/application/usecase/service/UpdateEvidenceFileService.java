package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incude.gsmc.v2.domain.evidence.application.usecase.UpdateEvidenceFileUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateEvidenceFileService implements UpdateEvidenceFileUseCase {

    private final S3Port s3Port;
    private final ActivityEvidencePersistencePort activityEvidencePersistencePort;
    private final OtherEvidencePersistencePort otherEvidencePersistencePort;
    private final RetryTemplate retryTemplate;

    @Override
    public void execute(Long evidenceId, String fileName, InputStream inputStream, EvidenceType evidenceType) {
        long startTime = System.currentTimeMillis();

        long uploadStart = System.currentTimeMillis();
        String fileUrl = uploadFile(fileName, inputStream);
        long uploadEnd = System.currentTimeMillis();
        log.info("File upload duration: {} ms", uploadEnd - uploadStart);

        switch (evidenceType) {
            case MAJOR, HUMANITIES -> {
                ActivityEvidence ae = activityEvidencePersistencePort.findActivityEvidenceById(evidenceId);

                long saveStart = System.currentTimeMillis();
                activityEvidencePersistencePort.saveActivityEvidence(ae.getId(), updateActivityEvidence(ae, fileUrl));
                long saveEnd = System.currentTimeMillis();
                log.info("ActivityEvidence save duration: {} ms", saveEnd - saveStart);
            }
            default -> {
                OtherEvidence oe = otherEvidencePersistencePort.findOtherEvidenceById(evidenceId);

                long saveStart = System.currentTimeMillis();
                otherEvidencePersistencePort.saveOtherEvidence(oe.getId(), updateOtherEvidence(oe, fileUrl));
                long saveEnd = System.currentTimeMillis();
                log.info("OtherEvidence save duration: {} ms", saveEnd - saveStart);
            }
        }

        long endTime = System.currentTimeMillis();
        log.info("Total execution duration: {} ms", endTime - startTime);
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
