package team.incude.gsmc.v2.domain.auth.application.port;

/**
 * 이메일 전송 기능을 추상화한 포트 인터페이스입니다.
 * <p>도메인 계층이 외부 이메일 발송 시스템에 직접 의존하지 않도록 하기 위해 설계되었으며,
 * 인증 코드 전송과 같은 기능을 어댑터 계층에 위임할 수 있도록 정의합니다.
 * <ul>
 *   <li>{@code sendEmail(String to, String authCode)} - 지정된 수신자에게 인증 코드를 포함한 이메일 전송</li>
 * </ul>
 * 실제 구현은 SMTP 외부 시스템에 따라 어댑터 계층에서 이루어집니다.
 * @author jihoonwjj
 */
public interface EmailPort {
    void sendEmail(String to, String authCode);
}
