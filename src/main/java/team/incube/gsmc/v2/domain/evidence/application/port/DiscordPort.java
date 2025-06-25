package team.incube.gsmc.v2.domain.evidence.application.port;

import team.incube.gsmc.v2.global.annotation.PortDirection;
import team.incube.gsmc.v2.global.annotation.port.Port;

/**
 * DiscordPort는 증빙자료 업로드 실패 시 Discord 웹훅을 통해 알림을 전송하는 포트 인터페이스입니다.
 * <p>이 인터페이스는 {@link SendEvidenceUploadFailureAlertUseCase}를 통해 알림 메시지를 전송합니다.
 * <p>알림 전송 실패 시에도 시스템 전체에 영향을 주지 않도록 예외를 처리합니다.
 * @author suuuuuuminnnnnn
 */
@Port(direction = PortDirection.OUTBOUND)
public interface DiscordPort {
    void sendEvidenceUploadFailureAlert(Long evidenceId, String fileName, String email, Throwable exception);
}
