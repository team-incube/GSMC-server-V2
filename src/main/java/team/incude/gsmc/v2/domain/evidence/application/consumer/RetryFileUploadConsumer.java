package team.incude.gsmc.v2.domain.evidence.application.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.evidence.application.port.EvidenceApplicationPort;
import team.incude.gsmc.v2.domain.evidence.application.port.RetryUploadZSetPort;
import team.incude.gsmc.v2.domain.evidence.presentation.data.FileUploadRetryMessageDto;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class RetryFileUploadConsumer {

    private final RetryUploadZSetPort retryUploadZSetPort;
    private final EvidenceApplicationPort evidenceApplicationPort;

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
                    retryUploadZSetPort.scheduleRetry(message, System.currentTimeMillis() + 60_000); // 1분 뒤 재등록
                } catch (Exception e) {
                    log.error("단일 재시도 메시지 처리 중 예외 발생", e);
                }
            }
        } catch (Exception e) {
            log.error("스케줄링 전체 처리 중 예외 발생", e);
        }
    }
}
