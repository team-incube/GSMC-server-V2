package team.incude.gsmc.v2.global.security.jwt.application.usecase;

import team.incude.gsmc.v2.domain.member.domain.constant.MemberRole;
import team.incude.gsmc.v2.global.security.jwt.data.TokenDto;

public interface JwtIssueUseCase {
    TokenDto issueAccessToken(String email, MemberRole roles);

    TokenDto issueRefreshToken(String email);
}
