package team.incude.gsmc.v2.domain.auth.application.usecase;

import team.incude.gsmc.v2.domain.auth.presentation.data.response.AuthTokenResponse;

/**
 * 사용자 인증을 위한 로그인 유스케이스 인터페이스입니다.
 * <p>이메일과 비밀번호를 통해 사용자를 인증하고, 성공 시 액세스 및 리프레시 토큰을 발급합니다.
 * 이 유스케이스는 주로 Web 어댑터를 통해 호출되며, {@code AuthApplicationPort}에서 이 유스케이스를 위임받아 실행됩니다.
 * @author snowykte0426
 */
public interface SignInUseCase {
    AuthTokenResponse execute(String email, String password);
}
