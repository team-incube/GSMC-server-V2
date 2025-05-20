package team.incude.gsmc.v2.domain.auth.persistence;

import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.auth.application.port.AuthApplicationPort;
import team.incude.gsmc.v2.domain.auth.application.usecase.*;
import team.incude.gsmc.v2.domain.auth.presentation.data.response.AuthTokenResponse;
import team.incude.gsmc.v2.domain.auth.application.usecase.ChangePasswordUseCase;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

/**
 * 인증 관련 유스케이스를 실행하는 애플리케이션 어댑터 클래스입니다.
 * <p>{@link AuthApplicationPort}를 구현하며, Web 어댑터 계층에서 전달된 인증 요청을
 * 실제 유스케이스 실행으로 위임합니다.
 * <p>담당 기능:
 * <ul>
 *   <li>회원 가입</li>
 *   <li>로그인</li>
 *   <li>토큰 갱신</li>
 *   <li>이메일 인증</li>
 *   <li>인증 코드 발송</li>
 *   <li>비밀번호 변경</li>
 * </ul>
 * 각 기능은 의존성 주입된 유스케이스 클래스에 위임됩니다.
 * @author jihoonwjj
 */
@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class AuthApplicationAdapter implements AuthApplicationPort {

    private final SignInUseCase signInUseCase;
    private final SignUpUseCase signUpUseCase;
    private final RefreshUseCase refreshUseCase;
    private final VerifyEmailUseCase verifyEmailUseCase;
    private final SendAuthenticationEmailUseCase sendAuthenticationEmailUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;

    /**
     * 회원 가입 요청을 실행합니다.
     * @param name 사용자 이름
     * @param email 사용자 이메일
     * @param password 비밀번호
     */
    @Override
    public void signUp(String name, String email, String password) {
        signUpUseCase.execute(name, email, password);
    }

    /**
     * 로그인 요청을 실행하고 인증 토큰을 반환합니다.
     * @param email 사용자 이메일
     * @param password 사용자 비밀번호
     * @return 인증 토큰 응답
     */
    @Override
    public AuthTokenResponse signIn(String email, String password) {
        return signInUseCase.execute(email, password);
    }

    /**
     * 리프레시 토큰을 기반으로 새 인증 토큰을 발급합니다.
     * @param refreshToken 클라이언트의 리프레시 토큰
     * @return 새로 발급된 인증 토큰
     */
    @Override
    public AuthTokenResponse refresh(String refreshToken) {
        return refreshUseCase.execute(refreshToken);
    }

    /**
     * 사용자가 입력한 인증 코드를 검증합니다.
     * @param code 인증 코드
     */
    @Override
    public void verifyEmail(String code) {
        verifyEmailUseCase.execute(code);
    }

    /**
     * 사용자 이메일로 인증 코드를 발송합니다.
     * @param email 수신자 이메일
     */
    @Override
    public void sendAuthenticationEmail(String email) {
        sendAuthenticationEmailUseCase.execute(email);
    }

    /**
     * 사용자 비밀번호를 새 비밀번호로 변경합니다.
     * @param email 사용자 이메일
     * @param newPassword 새 비밀번호
     */
    @Override
    public void changePassword(String email, String newPassword) { changePasswordUseCase.execute(email, newPassword); }
}
