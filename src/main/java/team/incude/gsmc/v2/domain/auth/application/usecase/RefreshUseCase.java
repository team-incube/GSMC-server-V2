package team.incude.gsmc.v2.domain.auth.application.usecase;

import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.auth.exception.RefreshTokenInvalidException;
import team.incude.gsmc.v2.domain.auth.presentation.data.response.AuthTokenResponse;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.member.exception.MemberNotFoundException;
import team.incude.gsmc.v2.global.annotation.usecase.UseCaseWithReadOnlyTransaction;
import team.incude.gsmc.v2.global.security.jwt.dto.TokenDto;
import team.incude.gsmc.v2.global.security.jwt.service.JwtIssueService;
import team.incude.gsmc.v2.global.security.jwt.service.JwtParserService;
import team.incude.gsmc.v2.global.security.jwt.service.JwtRefreshManagementService;

@UseCaseWithReadOnlyTransaction
@RequiredArgsConstructor
public class RefreshUseCase {

    private final JwtIssueService jwtIssueService;
    private final JwtParserService jwtParserService;
    private final JwtRefreshManagementService jwtRefreshManagementService;
    private final MemberPersistencePort memberPersistencePort;

    public AuthTokenResponse execute(String refreshToken) {
        if(jwtParserService.validateRefreshToken(refreshToken)) {
            String email = jwtParserService.getEmailFromRefreshToken(refreshToken);
            Member member = memberPersistencePort.findMemberByEmail(email).orElseThrow(MemberNotFoundException::new);
            jwtRefreshManagementService.deleteRefreshToken(refreshToken);
            TokenDto newAccessToken = jwtIssueService.issueAccessToken(email, member.getRole());
            TokenDto newRefreshToken = jwtIssueService.issueRefreshToken(email);
            return new AuthTokenResponse(
                    newAccessToken.token(),
                    newRefreshToken.token(),
                    newAccessToken.expiration(),
                    newRefreshToken.expiration(),
                    member.getRole());
        } else {
            throw new RefreshTokenInvalidException();
        }
    }
}
