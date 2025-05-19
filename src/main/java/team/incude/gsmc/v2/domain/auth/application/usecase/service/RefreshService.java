package team.incude.gsmc.v2.domain.auth.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.auth.application.usecase.RefreshUseCase;
import team.incude.gsmc.v2.domain.auth.exception.RefreshTokenInvalidException;
import team.incude.gsmc.v2.domain.auth.presentation.data.response.AuthTokenResponse;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.global.security.jwt.presentation.dto.TokenDto;
import team.incude.gsmc.v2.global.security.jwt.application.usecase.JwtIssueUseCase;
import team.incude.gsmc.v2.global.security.jwt.application.usecase.JwtParserUseCase;
import team.incude.gsmc.v2.global.security.jwt.application.usecase.JwtRefreshManagementUseCase;

@Service
@RequiredArgsConstructor
public class RefreshService implements RefreshUseCase {

    private final JwtIssueUseCase jwtIssueUseCase;
    private final JwtParserUseCase jwtParserUseCase;
    private final JwtRefreshManagementUseCase jwtRefreshManagementUseCase;
    private final MemberPersistencePort memberPersistencePort;

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
