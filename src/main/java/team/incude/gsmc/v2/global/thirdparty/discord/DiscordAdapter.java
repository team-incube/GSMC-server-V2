package team.incude.gsmc.v2.global.thirdparty.discord;

import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.evidence.application.port.DiscordPort;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;
import team.incude.gsmc.v2.global.thirdparty.discord.service.SendEvidenceUploadFailureAlertService;

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class DiscordAdapter implements DiscordPort {

    private final SendEvidenceUploadFailureAlertService fileUploadDiscordAlertService;

    @Override
    public void sendEvidenceUploadFailureAlert(Long evidenceId, String fileName, String email, Throwable exception) {
        fileUploadDiscordAlertService.sendEvidenceUploadFailureAlert(evidenceId, fileName, email, exception);
    }
}
