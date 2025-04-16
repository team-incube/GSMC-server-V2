package team.incude.gsmc.v2.domain.auth.application.port;

import team.incude.gsmc.v2.domain.auth.presentation.data.response.AuthTokenResponse;

public interface AuthApplicationPort {
    void signUp(String name, String email, String password);

    AuthTokenResponse signIn(String email, String password);

    AuthTokenResponse refresh(String refreshToken);

    void verifyEmail(String code);

    void sendAuthenticationEmail(String email);
}
