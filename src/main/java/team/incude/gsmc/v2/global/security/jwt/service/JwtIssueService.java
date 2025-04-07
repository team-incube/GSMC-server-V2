package team.incude.gsmc.v2.global.security.jwt.service;

import team.incude.gsmc.v2.domain.member.domain.constant.MemberRole;
import team.incude.gsmc.v2.global.security.jwt.dto.TokenDto;

public interface JwtIssueService {
    TokenDto issueAccessToken(String email, MemberRole roles);

    TokenDto issueRefreshToken(String email);
}
