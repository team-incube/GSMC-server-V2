package team.incude.gsmc.v2.domain.auth.application.port;

import jakarta.servlet.http.HttpServletRequest;
import team.incude.gsmc.v2.domain.member.domain.constant.MemberRole;
import team.incude.gsmc.v2.global.security.jwt.dto.TokenDto;

/**
 * Jwt 관련 유스케이스를 정의하는 포트 인터페이스입니다.
 * <p> 토큰발급, 유효성검사, 이메일추출 등 토큰 관련 기능을 제공합니다.
 * 주요기능:
 * <ul>
 *    <li>{@code issueAccessToken} - 새로운 액세스토큰 발급 </li>
 *    <li>{@code issueRefreshToken} - 새로운 리프레시토큰 발급 </li>
 *    <li>{@code validateAccessToken} - 액세스토큰의 유효성 검사 </li>
 *    <li>{@code validateRefreshToken} - 리프레시토클의 유효성 검사 </li>
 *    <li>{@code getEmailFromAccessToken} - 액세스토큰 이메일 추출 </li>
 *    <li>{@code getEmailFromRefreshToken} - 리프레시토큰 이메일 추출 </li>
 *    <li>{@code getRolesFromAccessToken} - 액세스토큰에서 역할 추출 </li>
 *    <li>{@code resolveToken} - 토큰 파싱 </li>
 * </ul>
 * @author jihoonwjj
 */
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
