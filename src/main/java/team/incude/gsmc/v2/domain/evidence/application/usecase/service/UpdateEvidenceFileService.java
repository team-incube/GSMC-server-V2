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

/**
 * 증빙자료의 파일 업로드를 처리하는 유스케이스 구현체입니다.
 * <p>{@link UpdateEvidenceFileUseCase}를 구현하며,
 * 증빙자료의 S3 업로드를 수행하고, 업로드 실패 시 {@link RetryUploadCommand}를 생성하여
 * Redis 기반 재시도 큐에 등록합니다.</p>
 * <p>성공적으로 업로드되면 증빙자료의 이미지 URL 또는 파일 URI를 갱신합니다.
 * {@link RetryTemplate}을 이용하여 업로드를 재시도하며,
 * {@link DiscordPort}를 통해 실패 알림도 전송됩니다.</p>
 * <p>{@code REQUIRES_NEW} 트랜잭션을 사용하여 재시도 로직과 독립적인 트랜잭션으로 처리합니다.</p>
 * @author suuuuuuminnnnnn
 */
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

    /**
     * 증빙자료의 파일을 업로드하고, 업로드 URL을 해당 증빙에 반영합니다.
     * <p>업로드에 실패하면 InputStream을 복제하여 {@link RetryUploadCommand}로 재시도 큐에 등록되며,
     * {@link DiscordPort}를 통해 실패 알림이 전송됩니다.</p>
     * @param evidenceId   대상 증빙자료 ID
     * @param fileName     업로드할 파일명
     * @param inputStream  업로드할 파일의 InputStream
     * @param evidenceType 증빙자료 유형
     * @param email        업로드 요청자의 이메일 (실패 알림 및 재시도 메시지용)
     * @throws S3UploadFailedException 업로드 실패 시 던져짐
     */
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
