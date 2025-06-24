package team.incude.gsmc.v2.domain.evidence.application.port;

import team.incude.gsmc.v2.domain.evidence.presentation.data.FileUploadRetryMessageDto;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.util.Set;

@Port(direction = PortDirection.INBOUND)
public interface RetryUploadZSetPort {
    void scheduleRetry(FileUploadRetryMessageDto message, long retryTimeMillis);

    Set<FileUploadRetryMessageDto> pollReadyToRetry(long nowMillis);

    void removeMessage(FileUploadRetryMessageDto message);
}
