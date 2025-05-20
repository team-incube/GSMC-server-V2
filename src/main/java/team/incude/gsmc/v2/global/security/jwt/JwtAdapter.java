package team.incude.gsmc.v2.global.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.member.domain.constant.MemberRole;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;
import team.incude.gsmc.v2.domain.auth.application.port.JwtPort;
import team.incude.gsmc.v2.global.security.jwt.application.usecase.JwtIssueUseCase;
import team.incude.gsmc.v2.global.security.jwt.application.usecase.JwtParserUseCase;
import team.incude.gsmc.v2.global.security.jwt.application.usecase.JwtRefreshManagementUseCase;
import team.incude.gsmc.v2.global.security.jwt.data.TokenDto;

@Adapter(direction = PortDirection.INBOUND)
@RequiredArgsConstructor
public class JwtAdapter implements JwtPort {

    private final JwtIssueUseCase jwtIssueUseCase;
    private final JwtParserUseCase jwtParserUseCase;
    private final JwtRefreshManagementUseCase jwtRefreshManagementUseCase;

    public TokenDto issueAccessToken(String email, MemberRole roles) {
        return jwtIssueUseCase.issueAccessToken(email, roles);
    }

    public TokenDto issueRefreshToken(String email) {
        return jwtIssueUseCase.issueRefreshToken(email);
    }

    public Boolean validateAccessToken(String token) {
        return jwtParserUseCase.validateAccessToken(token);
    }

    public Boolean validateRefreshToken(String token) {
        return jwtParserUseCase.validateRefreshToken(token);
    }

    public String getEmailFromAccessToken(String token) {
        return jwtParserUseCase.getEmailFromAccessToken(token);
    }

    public String getEmailFromRefreshToken(String token) {
        return jwtParserUseCase.getEmailFromRefreshToken(token);
    }

    public MemberRole getRolesFromAccessToken(String token) {
        return jwtParserUseCase.getRolesFromAccessToken(token);
    }

    public String resolveToken(HttpServletRequest request) {
        return jwtParserUseCase.resolveToken(request);
    }

    public void deleteRefreshToken(String refreshToken) {
        jwtRefreshManagementUseCase.deleteRefreshToken(refreshToken);
    }
}
