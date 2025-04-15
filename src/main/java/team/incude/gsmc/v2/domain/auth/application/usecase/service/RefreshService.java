package team.incude.gsmc.v2.domain.auth.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.auth.application.usecase.RefreshUseCase;
import team.incude.gsmc.v2.domain.auth.exception.RefreshTokenInvalidException;
import team.incude.gsmc.v2.domain.auth.presentation.data.response.AuthTokenResponse;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.global.security.jwt.dto.TokenDto;
import team.incude.gsmc.v2.global.security.jwt.service.JwtIssueService;
import team.incude.gsmc.v2.global.security.jwt.service.JwtParserService;
import team.incude.gsmc.v2.global.security.jwt.service.JwtRefreshManagementService;

@Service
@RequiredArgsConstructor
public class RefreshService implements RefreshUseCase {

    private final JwtIssueService jwtIssueService;
    private final JwtParserService jwtParserService;
    private final JwtRefreshManagementService jwtRefreshManagementService;
    private final MemberPersistencePort memberPersistencePort;

    public AuthTokenResponse execute(String refreshToken) {
        if (jwtParserService.validateRefreshToken(refreshToken)) {
            String email = jwtParserService.getEmailFromRefreshToken(refreshToken);
            Member member = memberPersistencePort.findMemberByEmail(email);
            TokenDto newAccessToken = jwtIssueService.issueAccessToken(email, member.getRole());
            TokenDto newRefreshToken = jwtIssueService.issueRefreshToken(email);
            jwtRefreshManagementService.deleteRefreshToken(refreshToken);
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
