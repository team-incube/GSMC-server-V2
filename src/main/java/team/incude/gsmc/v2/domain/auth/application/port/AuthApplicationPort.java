package team.incude.gsmc.v2.domain.auth.application.port;

import jakarta.mail.MessagingException;
import team.incude.gsmc.v2.domain.auth.presentation.data.response.AuthTokenResponse;

public interface AuthApplicationPort {

    void signUp(String email, String password, String name);

    AuthTokenResponse signIn(String email, String password);

    AuthTokenResponse refresh(String refreshToken);

    void verifyEmail(String code);

    void sendAuthenticationEmail(String email) throws MessagingException;
}
