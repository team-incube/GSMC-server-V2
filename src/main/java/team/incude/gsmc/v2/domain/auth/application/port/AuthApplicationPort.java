package team.incude.gsmc.v2.domain.auth.application.port;

import team.incude.gsmc.v2.domain.auth.presentation.data.response.AuthTokenResponse;

/**
 * 인증(Auth) 관련 유스케이스를 정의하는 애플리케이션 포트 인터페이스입니다.
 * <p>회원 가입, 로그인, 토큰 갱신, 이메일 인증, 비밀번호 변경 등 인증 및 사용자 초기화 관련 기능을 제공합니다.
 * 이 포트는 Web 계층에서 도메인 계층으로 진입하는 진입점 역할을 하며,
 * {@code @Port(direction = PortDirection.INBOUND)}로 선언되는 것이 일반적입니다.
 * 주요 기능:
 * <ul>
 *   <li>{@code signUp} - 회원 가입</li>
 *   <li>{@code signIn} - 로그인</li>
 *   <li>{@code refresh} - 액세스 토큰 갱신</li>
 *   <li>{@code verifyEmail} - 이메일 인증 코드 검증</li>
 *   <li>{@code sendAuthenticationEmail} - 인증 이메일 발송</li>
 *   <li>{@code changePassword} - 비밀번호 변경</li>
 * </ul>
 * @author jihoonwjj
 */
public interface AuthApplicationPort {
    void signUp(String name, String email, String password);

    AuthTokenResponse signIn(String email, String password);

    AuthTokenResponse refresh(String refreshToken);

    void verifyEmail(String code);

    void sendAuthenticationEmail(String email);

    void changePassword(String email, String password);
}
