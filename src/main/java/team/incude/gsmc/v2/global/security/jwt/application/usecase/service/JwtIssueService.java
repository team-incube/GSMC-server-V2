package team.incude.gsmc.v2.global.security.jwt.application.usecase.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.member.domain.constant.MemberRole;
import team.incude.gsmc.v2.global.security.jwt.application.usecase.JwtIssueUseCase;
import team.incude.gsmc.v2.global.security.jwt.persistence.entity.RefreshTokenRedisEntity;
import team.incude.gsmc.v2.global.security.jwt.persistence.repository.RefreshTokenRedisRepository;
import team.incude.gsmc.v2.global.security.jwt.data.TokenDto;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * JWT 토큰 발급을 담당하는 유스케이스 구현 클래스입니다.
 * <p>{@link JwtIssueUseCase}를 구현하며, 액세스 토큰 및 리프레시 토큰을 발급하고,
 * 리프레시 토큰은 Redis 저장소에 영속화합니다.
 * <p>설정 값으로부터 비밀키와 만료 시간을 주입받아 사용하며,
 * 토큰 생성 시 JJWT 라이브러리를 사용합니다.
 *
 * <ul>
 *   <li>Access Token: 이메일과 역할 포함, 일정 시간 후 만료</li>
 *   <li>Refresh Token: 이메일 포함, Redis에 저장</li>
 * </ul>
 *
 * @author jihoonwjj
 */

@Service
@RequiredArgsConstructor
public class JwtIssueService implements JwtIssueUseCase {

    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    @Value("${jwt.access-token.secret}")
    private String accessTokenSecret;
    @Value("${jwt.refresh-token.secret}")
    private String refreshTokenSecret;
    @Value("${jwt.access-token.expiration}")
    private long accessTokenExpiration;
    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    private SecretKey accessTokenKey;
    private SecretKey refreshTokenKey;

    @PostConstruct
    public void init() {
        accessTokenKey = Keys.hmacShaKeyFor(accessTokenSecret.getBytes());
        refreshTokenKey = Keys.hmacShaKeyFor(refreshTokenSecret.getBytes());
    }

    @Override
    public TokenDto issueAccessToken(String email, MemberRole roles) {
        LocalDateTime expiration = LocalDateTime.now().plusSeconds(accessTokenExpiration);
        return new TokenDto(
                Jwts.builder()
                        .claim("sub", email)
                        .claim("role", roles)
                        .claim("iat", LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toEpochSecond())
                        .claim("exp", expiration.atZone(ZoneId.of("Asia/Seoul")).toEpochSecond())
                        .signWith(accessTokenKey)
                        .compact(),
                expiration
        );
    }

    @Override
    public TokenDto issueRefreshToken(String email) {
        LocalDateTime expiration = LocalDateTime.now().plusSeconds(refreshTokenExpiration);
        TokenDto token = new TokenDto(
                Jwts.builder()
                        .claim("sub", email)
                        .claim("iat", LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toEpochSecond())
                        .claim("exp", expiration.atZone(ZoneId.of("Asia/Seoul")).toEpochSecond())
                        .signWith(refreshTokenKey)
                        .compact(),
                expiration
        );
        refreshTokenRedisRepository.save(RefreshTokenRedisEntity.builder()
                .token(token.token())
                .email(email)
                .expiration((long) expiration.getSecond())
                .build()
        );
        return token;
    }
}
