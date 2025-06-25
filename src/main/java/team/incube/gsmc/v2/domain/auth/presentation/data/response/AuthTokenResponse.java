package team.incube.gsmc.v2.domain.auth.presentation.data.response;

import team.incube.gsmc.v2.domain.member.domain.constant.MemberRole;

import java.time.LocalDateTime;


/**
 * 인증 토큰 발급 결과를 클라이언트에 반환하는 DTO입니다.
 * <p>로그인 또는 토큰 재발급 시 사용되며, 액세스 토큰과 리프레시 토큰,
 * 그리고 각각의 만료 시각과 사용자 권한(Role)을 함께 제공합니다.
 * @param accessToken 발급된 액세스 토큰
 * @param refreshToken 발급된 리프레시 토큰
 * @param accessTokenExpiresAt 액세스 토큰 만료 시각
 * @param refreshTokenExpiresAt 리프레시 토큰 만료 시각
 * @param role 사용자 권한 정보
 * @author jihoonwjj
 */
public record AuthTokenResponse(
        String accessToken,
        String refreshToken,
        LocalDateTime accessTokenExpiresAt,
        LocalDateTime refreshTokenExpiresAt,
        MemberRole role
) {
}
