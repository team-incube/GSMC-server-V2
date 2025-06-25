package team.incube.gsmc.v2.global.security.jwt.application.usecase;

import jakarta.servlet.http.HttpServletRequest;
import team.incube.gsmc.v2.domain.member.domain.constant.MemberRole;

public interface JwtParserUseCase {
    Boolean validateAccessToken(String token);
    Boolean validateRefreshToken(String token);
    String getEmailFromAccessToken(String token);
    String getEmailFromRefreshToken(String token);
    MemberRole getRolesFromAccessToken(String token);
    String resolveToken(HttpServletRequest request);
}
