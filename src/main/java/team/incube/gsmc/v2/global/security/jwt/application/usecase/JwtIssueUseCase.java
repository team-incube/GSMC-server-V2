package team.incube.gsmc.v2.global.security.jwt.application.usecase;

import team.incube.gsmc.v2.domain.member.domain.constant.MemberRole;
import team.incube.gsmc.v2.global.security.jwt.data.TokenDto;

public interface JwtIssueUseCase {
    TokenDto issueAccessToken(String email, MemberRole roles);

    TokenDto issueRefreshToken(String email);
}
