package team.incude.gsmc.v2.domain.auth.presentation.data.response;

import team.incude.gsmc.v2.domain.member.domain.constant.MemberRole;

import java.time.LocalDateTime;

public record AuthTokenResponse(
        String accessToken,
        String refreshToken,
        LocalDateTime accessTokenExpiresAt,
        LocalDateTime refreshTokenExpiresAt,
        MemberRole role
) {
}
