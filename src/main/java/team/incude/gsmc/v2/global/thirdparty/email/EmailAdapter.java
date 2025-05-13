package team.incude.gsmc.v2.global.thirdparty.email;

import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.auth.application.port.EmailPort;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;
import team.incude.gsmc.v2.global.thirdparty.email.usecase.EmailSendUseCase;

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class EmailAdapter implements EmailPort {

    private final EmailSendUseCase emailSendUseCase;

    @Override
    public void sendEmail(String to, String authCode) {
        emailSendUseCase.execute(to, authCode);
    }
}
