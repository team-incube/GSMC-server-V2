package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import team.incude.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.DiscordPort;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incude.gsmc.v2.domain.evidence.application.usecase.UpdateEvidenceFileUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.global.thirdparty.aws.exception.S3UploadFailedException;

import java.io.InputStream;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UpdateEvidenceFileService implements UpdateEvidenceFileUseCase {

    private final S3Port s3Port;
    private final ActivityEvidencePersistencePort activityEvidencePersistencePort;
    private final OtherEvidencePersistencePort otherEvidencePersistencePort;
    private final RetryTemplate retryTemplate;
    private final DiscordPort discordPort;

    @Override
    public void execute(Long evidenceId, String fileName, InputStream inputStream, EvidenceType evidenceType, String email) {
        String fileUrl;

        fileUrl = retryTemplate.execute(context -> {
            try {
                return s3Port.uploadFile(fileName, inputStream).get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                discordPort.sendEvidenceUploadFailureAlert(
                        evidenceId,
                        fileName,
                        email,
                        e
                );
                throw new S3UploadFailedException();
            } catch (ExecutionException e) {
                discordPort.sendEvidenceUploadFailureAlert(
                        evidenceId,
                        fileName,
                        email,
                        e
                );
                throw new S3UploadFailedException();
            }
        });

        switch (evidenceType) {
            case MAJOR, HUMANITIES -> {
                ActivityEvidence ae = activityEvidencePersistencePort.findActivityEvidenceById(evidenceId);
                activityEvidencePersistencePort.saveActivityEvidence(ae.getId(), ActivityEvidence.builder()
                        .id(ae.getId())
                        .title(ae.getTitle())
                        .imageUrl(fileUrl)
                        .content(ae.getContent())
                        .build());
            }
            default -> {
                OtherEvidence oe = otherEvidencePersistencePort.findOtherEvidenceById(evidenceId);
                otherEvidencePersistencePort.saveOtherEvidence(oe.getId(), OtherEvidence.builder()
                        .id(oe.getId())
                        .fileUri(fileUrl)
                        .build());
            }
        }
    }
}
