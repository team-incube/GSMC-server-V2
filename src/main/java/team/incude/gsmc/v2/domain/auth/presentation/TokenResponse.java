package team.incude.gsmc.v2.domain.auth.presentation;

import team.incude.gsmc.v2.domain.member.domain.constant.MemberRole;

import java.time.LocalDateTime;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        LocalDateTime accessTokenExpireAt,
        LocalDateTime refreshTokenExpireAt,
        MemberRole role
) {
}
