package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import team.incude.gsmc.v2.domain.evidence.application.port.*;
import team.incude.gsmc.v2.domain.evidence.application.usecase.UpdateEvidenceFileUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.RetryUploadCommand;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.global.thirdparty.aws.exception.S3UploadFailedException;
import team.incude.gsmc.v2.global.util.InputStreamUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
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
    private final RedisPort redisPort;

    @Override
    public void execute(Long evidenceId, String fileName, InputStream inputStream, EvidenceType evidenceType, String email) {
        String fileUrl;

        fileUrl = retryTemplate.execute(context -> {
            try {
                return s3Port.uploadFile(fileName, inputStream).get();
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();

                ByteArrayInputStream retryInputStream;
                try {
                    retryInputStream = InputStreamUtil.duplicate(inputStream);
                } catch (IOException ioException) {
                    log.error("InputStream 복제 실패", ioException);
                    throw new S3UploadFailedException();
                }

                RetryUploadCommand command = RetryUploadCommand.builder()
                        .commandId(UUID.randomUUID().toString())
                        .evidenceId(evidenceId)
                        .fileName(fileName)
                        .tempFilePath(null)
                        .evidenceType(evidenceType)
                        .email(email)
                        .build();

                redisPort.scheduleRetry(command, retryInputStream, 5000);

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
