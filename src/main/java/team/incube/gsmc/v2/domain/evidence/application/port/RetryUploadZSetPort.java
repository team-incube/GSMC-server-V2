package team.incube.gsmc.v2.domain.evidence.application.port;

import team.incube.gsmc.v2.domain.evidence.presentation.data.FileUploadRetryMessageDto;
import team.incube.gsmc.v2.global.annotation.PortDirection;
import team.incube.gsmc.v2.global.annotation.port.Port;

import java.util.Set;

/**
 * 파일 업로드 실패 시 재시도 처리를 위한 Redis ZSet 기반 포트입니다.
 * <p>업로드 실패 파일을 Redis의 ZSet 등에 일정 시간 이후 재시도 대상으로 등록하고,
 * 조건이 충족되면 메시지를 폴링하여 실제 업로드 작업이 재시도될 수 있도록 지원합니다.</p>
 * 이 인터페이스는 {@code @Port} 어노테이션을 통해 OUTBOUND 방향으로 지정되어 있습니다.
 */
@Port(direction = PortDirection.OUTBOUND)
public interface RetryUploadZSetPort {
    void scheduleRetry(FileUploadRetryMessageDto message, long retryTimeMillis);

    Set<FileUploadRetryMessageDto> pollReadyToRetry(long nowMillis);

    void removeMessage(FileUploadRetryMessageDto message);
}
