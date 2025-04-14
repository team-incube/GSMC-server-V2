package team.incude.gsmc.v2.domain.auth.persistence;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.auth.application.port.AuthApplicationPort;
import team.incude.gsmc.v2.domain.auth.application.usecase.*;
import team.incude.gsmc.v2.domain.auth.presentation.data.response.AuthTokenResponse;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class AuthApplicationAdapter implements AuthApplicationPort {

    private final SignInUseCase signInUseCase;
    private final SignUpUseCase signUpUseCase;
    private final RefreshUseCase refreshUseCase;
    private final VerifyEmailUseCase verifyEmailUseCase;
    private final SendAuthenticationEmailUseCase sendAuthenticationEmailUseCase;

    @Override
    public void signUp(String email, String password, String name) {
        signUpUseCase.execute(email, password, name);
    }

    @Override
    public AuthTokenResponse signIn(String email, String password) {
        return signInUseCase.execute(email, password);
    }

    @Override
    public AuthTokenResponse refresh(String refreshToken) {
        return refreshUseCase.execute(refreshToken);
    }

    @Override
    public void verifyEmail(String code) {
        verifyEmailUseCase.execute(code);
    }

    @Override
    public void sendAuthenticationEmail(String email) throws MessagingException {
        sendAuthenticationEmailUseCase.execute(email);
    }
}
