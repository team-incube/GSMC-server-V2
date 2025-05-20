package team.incude.gsmc.v2.domain.auth.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.auth.application.usecase.RefreshUseCase;
import team.incude.gsmc.v2.domain.auth.exception.RefreshTokenInvalidException;
import team.incude.gsmc.v2.domain.auth.presentation.data.response.AuthTokenResponse;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.global.security.jwt.dto.TokenDto;
import team.incude.gsmc.v2.global.security.jwt.application.usecase.JwtIssueUseCase;
import team.incude.gsmc.v2.global.security.jwt.application.usecase.JwtParserUseCase;
import team.incude.gsmc.v2.global.security.jwt.application.usecase.JwtRefreshManagementUseCase;

/**
 * 리프레시 토큰을 검증하고 새로운 인증 토큰을 발급하는 유스케이스 구현 클래스입니다.
 * <p>{@link RefreshUseCase}를 구현하며, 전달된 리프레시 토큰이 유효한 경우
 * 이메일을 추출하고 새로운 액세스 및 리프레시 토큰을 발급한 후 기존 토큰은 폐기합니다.
 * <p>발급된 토큰은 {@link AuthTokenResponse} 형태로 반환되며, 사용자의 권한 정보도 포함됩니다.
 * 예외 발생 시 {@link RefreshTokenInvalidException}을 던집니다.
 * @author jihoonwjj
 */
@Service
@RequiredArgsConstructor
public class RefreshService implements RefreshUseCase {

    private final JwtIssueUseCase jwtIssueUseCase;
    private final JwtParserUseCase jwtParserUseCase;
    private final JwtRefreshManagementUseCase jwtRefreshManagementUseCase;
    private final MemberPersistencePort memberPersistencePort;

    /**
     * 리프레시 토큰을 검증하고 새로운 액세스 및 리프레시 토큰을 발급합니다.
     * <p>토큰 유효성 검사를 통과하면 이메일을 추출하고 해당 사용자에 대해 새로운 토큰을 생성합니다.
     * 이후 기존 리프레시 토큰을 폐기하고 새 토큰 정보를 반환합니다.
     * @param refreshToken 클라이언트로부터 전달된 리프레시 토큰
     * @return 새로운 인증 토큰 및 사용자 권한 정보를 포함한 응답 객체
     * @throws RefreshTokenInvalidException 토큰이 유효하지 않을 경우 발생
     */
    public AuthTokenResponse execute(String refreshToken) {
        if (jwtParserUseCase.validateRefreshToken(refreshToken)) {
            String email = jwtParserUseCase.getEmailFromRefreshToken(refreshToken);
            Member member = memberPersistencePort.findMemberByEmail(email);
            TokenDto newAccessToken = jwtIssueUseCase.issueAccessToken(email, member.getRole());
            TokenDto newRefreshToken = jwtIssueUseCase.issueRefreshToken(email);
            jwtRefreshManagementUseCase.deleteRefreshToken(refreshToken);
            return new AuthTokenResponse(
                    newAccessToken.token(),
                    newRefreshToken.token(),
                    newAccessToken.expiration(),
                    newRefreshToken.expiration(),
                    member.getRole()
            );
        } else {
            throw new RefreshTokenInvalidException();
        }
    }
}
