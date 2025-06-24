package team.incube.gsmc.v2.domain.evidence.application.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import team.incube.gsmc.v2.domain.evidence.application.port.EvidenceApplicationPort;
import team.incube.gsmc.v2.domain.evidence.application.port.RetryUploadZSetPort;
import team.incube.gsmc.v2.domain.evidence.presentation.data.FileUploadRetryMessageDto;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Set;

/**
 * Redis ZSet 기반 재시도 큐에 등록된 파일 업로드 요청을 소비하여 처리하는 컨슈머 클래스입니다.
 * <p>파일 업로드 실패 시 임시 파일 경로와 메타 정보를 {@link FileUploadRetryMessageDto} 형태로 ZSet에 저장하고,
 * 해당 시간이 도래하면 이를 읽어 업로드를 재시도합니다.</p>
 * <p>업로드 성공 시 메시지를 제거하고 임시 파일을 삭제하며,
 * 실패 시 지정된 지연 시간 이후 다시 재시도하도록 스케줄링합니다.</p>
 * @author suuuuuuminnnnnn
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RetryFileUploadConsumer {

    private final RetryUploadZSetPort retryUploadZSetPort;
    private final EvidenceApplicationPort evidenceApplicationPort;

    private static final long RETRY_DELAY_MILLIS = 60_000L;

    /**
     * 재시도 대상 파일 업로드 메시지를 주기적으로 확인하고, 업로드를 재시도합니다.
     * <p>1. 현재 시간 이전으로 예약된 메시지를 조회합니다.
     * <br>2. 임시 파일이 존재하지 않으면 메시지를 제거하고 스킵합니다.
     * <br>3. 파일이 존재하면 다시 업로드 시도 후, 성공 시 삭제/제거, 실패 시 재스케줄합니다.</p>
     * <p>{@code @Scheduled} 애노테이션으로 10초마다 실행되며,
     * 시스템 오류가 발생하더라도 전체 처리가 중단되지 않도록 내부 예외 처리를 포함합니다.</p>
     */
    @Scheduled(fixedDelay = 10_000)
    public void consume() {
        try {
            long now = System.currentTimeMillis();
            Set<FileUploadRetryMessageDto> messageDtos = retryUploadZSetPort.pollReadyToRetry(now);

            for (FileUploadRetryMessageDto message : messageDtos) {
                try {
                    File file = new File(message.tempFilePath());

                    if (!file.exists()) {
                        log.warn("임시 파일이 존재하지 않습니다. 재시도 생략: {}", message.tempFilePath());
                        retryUploadZSetPort.removeMessage(message);
                        continue;
                    }

                    try (InputStream inputStream = Files.newInputStream(file.toPath())) {
                        evidenceApplicationPort.updateEvidenceFile(
                                message.evidenceId(),
                                message.fileName(),
                                inputStream,
                                message.evidenceType(),
                                message.email()
                        );

                        retryUploadZSetPort.removeMessage(message);

                        if (!file.delete()) {
                            log.warn("임시 파일 삭제 실패: {}", file.getAbsolutePath());
                        }
                    }
                } catch (IOException e) {
                    log.error("업로드 재시도 실패", e);
                    retryUploadZSetPort.scheduleRetry(message, System.currentTimeMillis() + RETRY_DELAY_MILLIS);
                } catch (Exception e) {
                    log.error("단일 재시도 메시지 처리 중 예외 발생", e);
                }
            }
        } catch (Exception e) {
            log.error("스케줄링 전체 처리 중 예외 발생", e);
        }
    }
}
