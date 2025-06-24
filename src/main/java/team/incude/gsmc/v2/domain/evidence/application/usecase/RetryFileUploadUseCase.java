package team.incude.gsmc.v2.domain.evidence.application.usecase;

import team.incude.gsmc.v2.domain.evidence.domain.RetryUploadCommand;

import java.io.InputStream;

/**
 * 파일 업로드 재시도 유스케이스 인터페이스입니다.
 * <p>재시도 커맨드와 파일 스트림, 지연 시간을 받아 재시도 큐에 등록하거나 즉시 재시도를 수행합니다.
 * @author suuuuuuminnnnnn
 */
public interface RetryFileUploadUseCase {
    void execute(RetryUploadCommand retryUploadCommand, InputStream inputStream, long delayMillis);
}
