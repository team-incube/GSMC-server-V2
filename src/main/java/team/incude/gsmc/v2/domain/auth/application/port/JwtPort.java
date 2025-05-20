package team.incude.gsmc.v2.domain.auth.application.port;

import jakarta.servlet.http.HttpServletRequest;
import team.incude.gsmc.v2.domain.member.domain.constant.MemberRole;
import team.incude.gsmc.v2.global.security.jwt.dto.TokenDto;

public interface JwtPort {
    TokenDto issueAccessToken(String email, MemberRole roles);

    TokenDto issueRefreshToken(String email);

    Boolean validateAccessToken(String accessToken);

    Boolean validateRefreshToken(String refreshToken);

    String getEmailFromAccessToken(String accessToken);

    String getEmailFromRefreshToken(String refreshToken);

    MemberRole getRolesFromAccessToken(String accessToken);

    String resolveToken(HttpServletRequest request);
}
