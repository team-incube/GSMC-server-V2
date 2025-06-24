package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incude.gsmc.v2.domain.evidence.application.port.RetryUploadZSetPort;
import team.incude.gsmc.v2.domain.evidence.application.usecase.RetryFileUploadUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.RetryUploadCommand;
import team.incude.gsmc.v2.domain.evidence.presentation.data.FileUploadRetryMessageDto;
import team.incude.gsmc.v2.global.redis.mapper.RetryUploadMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * S3 업로드 실패 시 파일을 임시 저장하고 재시도 큐에 등록하는 유스케이스 구현 클래스입니다.
 * <p>임시 파일로 저장한 후, 해당 경로를 포함하는 메시지를 Redis ZSet에 등록하여
 * 일정 시간 이후 재업로드가 시도되도록 구성됩니다.
 * <p>파일 복사 중 예외 발생 시 임시 파일을 삭제하고, 재시도 등록 실패 로그를 출력합니다.
 * @author suuuuuuminnnnnn
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RetryFileUploadService implements RetryFileUploadUseCase {

    private final RetryUploadMapper retryUploadMapper;
    private final RetryUploadZSetPort retryUploadZSetPort;

    /**
     * S3 업로드 실패 시 파일을 임시 경로에 복사하고, 재시도 큐(Redis ZSet)에 등록합니다.
     * <ul>
     *     <li>임시 디렉터리에 파일을 저장합니다 (시스템 기본 temp 디렉토리).</li>
     *     <li>경로 포함 정보를 메시지로 만들어 Redis에 등록합니다.</li>
     *     <li>예외 발생 시 임시 파일은 삭제됩니다.</li>
     * </ul>
     * @param command 업로드 실패 관련 정보(evidenceId, 파일 이름 등)
     * @param inputStream 업로드 실패한 파일의 스트림
     * @param delayMillis 재시도까지 대기할 시간 (ms)
     * @throws RuntimeException 파일 복사나 등록 중 예외가 발생한 경우
     */
    @Override
    public void execute(RetryUploadCommand command, InputStream inputStream, long delayMillis) {
        Path tempFile = null;

        try {
            tempFile = Files.createTempFile("evidence-", "-" + command.getFileName());

            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

            RetryUploadCommand commandWithPath = RetryUploadCommand.builder()
                    .commandId(command.getCommandId())
                    .evidenceId(command.getEvidenceId())
                    .fileName(command.getFileName())
                    .tempFilePath(tempFile.toAbsolutePath().toString())
                    .evidenceType(command.getEvidenceType())
                    .email(command.getEmail())
                    .build();

            FileUploadRetryMessageDto message = retryUploadMapper.toMessage(commandWithPath);
            retryUploadZSetPort.scheduleRetry(message, System.currentTimeMillis() + delayMillis);

        } catch (IOException e) {
            if (tempFile != null && Files.exists(tempFile)) {
                try {
                    Files.delete(tempFile);
                } catch (IOException deleteEx) {
                    log.warn("임시 파일 삭제 실패: {}", tempFile.toAbsolutePath(), deleteEx);
                }
            }
            log.error("파일 복사 및 재시도 큐 등록 중 예외 발생", e);
            throw new RuntimeException("파일 재시도 큐 등록 실패", e);
        }
    }
}
