package team.incube.gsmc.v2.global.thirdparty.email;

import lombok.RequiredArgsConstructor;
import team.incube.gsmc.v2.domain.auth.application.port.EmailPort;
import team.incube.gsmc.v2.global.annotation.PortDirection;
import team.incube.gsmc.v2.global.annotation.adapter.Adapter;
import team.incube.gsmc.v2.global.thirdparty.email.usecase.EmailSendUseCase;

/**
 * 이메일 전송 기능을 외부 시스템에 위임하는 어댑터 클래스입니다.
 * <p>{@link EmailPort}를 구현하며, 실제 메일 발송은 {@link EmailSendUseCase}를 통해 처리됩니다.
 * 도메인 계층은 이 어댑터를 통해 외부 이메일 전송 로직과 분리되어 동작합니다.
 * 이 클래스는 OUTBOUND 어댑터로서 외부 시스템(SMTP, SendGrid 등)에 요청을 위임하는 역할을 합니다.
 * @author jihoonwjj
 */
@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class EmailAdapter implements EmailPort {

    private final EmailSendUseCase emailSendUseCase;

    /**
     * 인증 코드를 포함한 이메일을 외부 이메일 발송 유스케이스를 통해 전송합니다.
     * @param to 수신자 이메일 주소
     * @param authCode 전송할 인증 코드
     */
    @Override
    public void sendEmail(String to, String authCode) {
        emailSendUseCase.execute(to, authCode);
    }
}
