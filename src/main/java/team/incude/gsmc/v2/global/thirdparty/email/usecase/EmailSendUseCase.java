package team.incude.gsmc.v2.global.thirdparty.email.usecase;

/**
 * 이메일 전송 유스케이스를 정의하는 인터페이스입니다.
 * <p>인증 코드 발송을 포함한 이메일 전송 기능을 추상화하며,
 * 어댑터 계층이 이를 구현하여 외부 메일 서버와 통신합니다.
 * @author jihoonwjj
 */
public interface EmailSendUseCase {
    void execute(String to, String authCode);
}
