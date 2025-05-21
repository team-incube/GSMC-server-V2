package team.incude.gsmc.v2.domain.auth.application.port;

import jakarta.servlet.http.HttpServletRequest;
import team.incude.gsmc.v2.domain.member.domain.constant.MemberRole;
import team.incude.gsmc.v2.global.security.jwt.data.TokenDto;

/**
 * JWT 관련 기능을 정의하는 포트 인터페이스입니다.
 * <p>도메인 계층에서 JWT 토큰 발급, 검증, 파싱 등의 기능을 추상화하여
 * 어댑터 계층(JwtAdapter 등)과의 의존성을 줄입니다.
 * <p>담당 기능:
 * <ul>
 *   <li>Access/Refresh Token 발급</li>
 *   <li>Token 유효성 검증</li>
 *   <li>Token 파싱 (이메일, 권한 정보 추출)</li>
 *   <li>HTTP 요청에서 Token 추출</li>
 *   <li>Refresh Token 삭제</li>
 * </ul>
 * 해당 포트를 구현하는 클래스는 실제 JWT 처리 로직을 담고 있어야 합니다.
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

    void deleteRefreshToken(String refreshToken);
}
