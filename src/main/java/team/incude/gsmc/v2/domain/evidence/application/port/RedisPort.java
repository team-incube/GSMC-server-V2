package team.incude.gsmc.v2.domain.evidence.application.port;

import team.incude.gsmc.v2.domain.evidence.domain.RetryUploadCommand;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.io.InputStream;

/**
 * 파일 업로드 실패 시 재시도 처리를 위한 Redis 기반 포트입니다.
 * <p>업로드 실패 파일을 일정 시간 이후 재시도할 수 있도록 Redis ZSet 또는 유사한 자료구조에 등록합니다.
 * <br>재시도 시 필요한 파일 스트림, 메타 정보, 실행 시간을 함께 저장하여, 소비자가 재시도 처리를 수행할 수 있게 합니다.</p>
 * 이 인터페이스는 {@code @Port} 어노테이션을 통해 INBOUND 방향으로 지정되어 있습니다.
 */
@Port(direction = PortDirection.INBOUND)
public interface RedisPort {
    void scheduleRetry(RetryUploadCommand command, InputStream inputStream, long retryTimeMillis);
}
