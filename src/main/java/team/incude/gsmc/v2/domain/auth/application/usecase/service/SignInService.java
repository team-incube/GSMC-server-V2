package team.incude.gsmc.v2.domain.auth.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.auth.application.usecase.SignInUseCase;
import team.incude.gsmc.v2.domain.auth.exception.PasswordInvalidException;
import team.incude.gsmc.v2.domain.auth.presentation.data.response.AuthTokenResponse;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.global.security.jwt.presentation.dto.TokenDto;
import team.incude.gsmc.v2.global.security.jwt.application.usecase.JwtIssueUseCase;

/**
 * 로그인 요청을 처리하는 유스케이스 구현 클래스입니다.
 * <p>{@link SignInUseCase}를 구현하며, 이메일과 비밀번호를 검증한 뒤
 * 성공 시 JWT 액세스 및 리프레시 토큰을 생성하여 반환합니다.
 * <p>비밀번호 검증은 {@link BCryptPasswordEncoder}를 통해 수행되며,
 * 실패 시 {@link PasswordInvalidException}을 발생시킵니다.
 * <p>토큰 발급은 {@link JwtIssueUseCase}를 통해 처리되며,
 * 최종 응답은 {@link AuthTokenResponse} 형태로 반환됩니다.
 * @author jihoonwjj
 */
@Service
@RequiredArgsConstructor
public class SignInService implements SignInUseCase {

    private final JwtIssueUseCase jwtIssueUseCase;
    private final MemberPersistencePort memberPersistencePort;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 이메일과 비밀번호를 검증하고, 유효할 경우 인증 토큰을 발급합니다.
     * <p>비밀번호가 일치하면 {@link JwtIssueUseCase}를 통해 액세스 토큰과 리프레시 토큰을 생성하여
     * {@link AuthTokenResponse}로 반환합니다. 일치하지 않을 경우 {@link PasswordInvalidException}이 발생합니다.
     * @param email 로그인 요청자의 이메일
     * @param password 로그인 요청자의 비밀번호
     * @return 인증 토큰 응답 객체
     * @throws PasswordInvalidException 비밀번호가 일치하지 않을 경우
     */
    public AuthTokenResponse execute(String email, String password) {
        Member member = memberPersistencePort.findMemberByEmail(email);
        if (bCryptPasswordEncoder.matches(password, member.getPassword())) {
            TokenDto accessToken = jwtIssueUseCase.issueAccessToken(member.getEmail(), member.getRole());
            TokenDto refreshToken = jwtIssueUseCase.issueRefreshToken(member.getEmail());
            return new AuthTokenResponse(
                    accessToken.token(),
                    refreshToken.token(),
                    accessToken.expiration(),
                    refreshToken.expiration(),
                    member.getRole()
            );
        } else {
            throw new PasswordInvalidException();
        }
    }
}
