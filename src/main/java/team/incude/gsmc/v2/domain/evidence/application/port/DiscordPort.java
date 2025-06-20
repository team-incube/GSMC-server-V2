package team.incude.gsmc.v2.domain.evidence.application.port;

import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

@Port(direction = PortDirection.INBOUND)
public interface DiscordPort {
    void sendEvidenceUploadFailureAlert(Long evidenceId, String fileName, String email, Throwable exception);
}
