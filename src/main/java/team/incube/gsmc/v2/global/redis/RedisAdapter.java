package team.incube.gsmc.v2.global.redis;

import lombok.RequiredArgsConstructor;
import team.incube.gsmc.v2.domain.evidence.application.port.RedisPort;
import team.incube.gsmc.v2.domain.evidence.application.usecase.RetryFileUploadUseCase;
import team.incube.gsmc.v2.domain.evidence.domain.RetryUploadCommand;
import team.incube.gsmc.v2.global.annotation.PortDirection;
import team.incube.gsmc.v2.global.annotation.adapter.Adapter;

import java.io.InputStream;

/**
 * Redis 재시도 큐 관련 INBOUND 어댑터입니다.
 * {@link RetryFileUploadUseCase}를 호출하여 재시도 메시지를 큐에 등록합니다.
 * author suuuuuuminnnnnn
 */
@Adapter(direction = PortDirection.INBOUND)
@RequiredArgsConstructor
public class RedisAdapter implements RedisPort {

    private final RetryFileUploadUseCase retryFileUploadUseCase;

    /**
     * Redis 재시도 큐에 재시도 명령과 파일 입력 스트림을 등록합니다.
     * @param command 재시도 명령 데이터
     * @param inputStream 파일 입력 스트림
     * @param retryTimeMillis 재시도 예정 시간 (밀리초 단위)
     */
    @Override
    public void scheduleRetry(RetryUploadCommand command, InputStream inputStream, long retryTimeMillis) {
        retryFileUploadUseCase.execute(command, inputStream, retryTimeMillis);
    }
}
