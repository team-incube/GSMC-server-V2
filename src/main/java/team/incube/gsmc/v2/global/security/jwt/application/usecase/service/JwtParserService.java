package team.incube.gsmc.v2.global.security.jwt.application.usecase.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import team.incube.gsmc.v2.domain.member.domain.constant.MemberRole;
import team.incube.gsmc.v2.global.security.jwt.persistence.repository.RefreshTokenRedisRepository;
import team.incube.gsmc.v2.global.security.jwt.application.usecase.JwtParserUseCase;

import javax.crypto.SecretKey;

/**
 * JWT 파싱 및 유효성 검증 기능을 제공하는 유스케이스 구현 클래스입니다.
 * <p>{@link JwtParserUseCase}를 구현하며, 액세스 토큰 및 리프레시 토큰에서
 * 이메일, 권한 정보 추출과 유효성 검사, HTTP 요청에서의 토큰 해석 등을 수행합니다.
 * <p>기능 설명:
 * <ul>
 *   <li>토큰 유효성 검사</li>
 *   <li>이메일 및 권한 추출</li>
 *   <li>HTTP 요청에서 토큰 추출</li>
 * </ul>
 * <p>JJWT 라이브러리를 사용하여 서명된 JWT의 Claim을 파싱하며,
 * 리프레시 토큰의 경우 Redis 저장 여부도 함께 확인합니다.
 * @author jihoonwjj
 */

@Service
@RequiredArgsConstructor
public class JwtParserService implements JwtParserUseCase {
    @Value("${jwt.access-token.secret}")
    private String accessTokenSecret;
    @Value("${jwt.refresh-token.secret}")
    private String refreshTokenSecret;
    private SecretKey accessTokenKey;
    private SecretKey refreshTokenKey;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @PostConstruct
    public void init() {
        accessTokenKey = Keys.hmacShaKeyFor(accessTokenSecret.getBytes());
        refreshTokenKey = Keys.hmacShaKeyFor(refreshTokenSecret.getBytes());
    }

    @Override
    public Boolean validateAccessToken(String token) {
        try {
            parseAccessTokenClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean validateRefreshToken(String token) {
        try {
            parseRefreshTokenClaims(token);
            return refreshTokenRedisRepository.existsById(token);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getEmailFromAccessToken(String token) {
        return parseAccessTokenClaims(token).getSubject();
    }

    @Override
    public String getEmailFromRefreshToken(String token) {
        return parseRefreshTokenClaims(token).getSubject();
    }

    @Override
    public MemberRole getRolesFromAccessToken(String token) {
        return MemberRole.valueOf(parseAccessTokenClaims(token).get("role", String.class));
    }

    @Override
    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return (token != null && token.startsWith("Bearer ")) ? token.substring(7) : null;
    }

    private Claims parseAccessTokenClaims(String token) {
        return Jwts.parser()
                .verifyWith(accessTokenKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Claims parseRefreshTokenClaims(String token) {
        return Jwts.parser()
                .verifyWith(refreshTokenKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
