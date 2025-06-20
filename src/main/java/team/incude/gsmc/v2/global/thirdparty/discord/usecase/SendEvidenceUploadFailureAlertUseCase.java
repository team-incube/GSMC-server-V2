package team.incude.gsmc.v2.global.thirdparty.discord.usecase;

/**
 * 증빙자료 업로드 실패 시 Discord 웹훅을 통해 알림을 전송하는 유스케이스 인터페이스입니다.
 * <p>이 인터페이스는 S3 파일 업로드 실패 시 관리자에게 즉시 알림을 제공하여
 * 빠른 대응이 가능하도록 합니다.
 * <p>구현 클래스에서는 Discord 웹훅 URL을 통해 HTTP POST 요청으로 메시지를 전송합니다.
 * <p>알림 전송 실패 시에도 시스템 전체에 영향을 주지 않도록 예외를 캐치하여 로그로만 기록합니다.
 * @author suuuuuuminnnnnn
 */
public interface SendEvidenceUploadFailureAlertUseCase {
    void sendEvidenceUploadFailureAlert(Long evidenceId, String fileName, String email, Throwable exception);
}
