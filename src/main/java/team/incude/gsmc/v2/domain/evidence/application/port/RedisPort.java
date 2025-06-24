package team.incude.gsmc.v2.domain.evidence.application.port;

import team.incude.gsmc.v2.domain.evidence.domain.RetryUploadCommand;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.io.InputStream;

@Port(direction = PortDirection.INBOUND)
public interface RedisPort {
    void scheduleRetry(RetryUploadCommand command, InputStream inputStream, long retryTimeMillis);
}
