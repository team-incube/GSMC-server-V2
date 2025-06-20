package team.incude.gsmc.v2.global.thirdparty.discord;

import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.evidence.application.port.DiscordPort;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;
import team.incude.gsmc.v2.global.thirdparty.discord.usecase.SendEvidenceUploadFailureAlertUseCase;

/**
 * DiscordAdapter는 증빙자료 업로드 실패 시 Discord 웹훅을 통해 알림을 전송하는 어댑터 클래스입니다.
 * <p>이 클래스는 {@link DiscordPort} 인터페이스를 구현하며, {@link SendEvidenceUploadFailureAlertUseCase}를 사용하여
 * 알림 메시지를 전송합니다. 어댑터는 OUTBOUND 방향으로 설정되어 있습니다.
 * <p>알림 전송 실패 시에도 시스템 전체에 영향을 주지 않도록 예외를 처리합니다.
 * @author suuuuuuminnnnnn
 */
@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class DiscordAdapter implements DiscordPort {

    private final SendEvidenceUploadFailureAlertUseCase sendEvidenceUploadFailureAlertUseCase;

    @Override
    public void sendEvidenceUploadFailureAlert(Long evidenceId, String fileName, String email, Throwable exception) {
        sendEvidenceUploadFailureAlertUseCase.sendEvidenceUploadFailureAlert(evidenceId, fileName, email, exception);
    }
}
