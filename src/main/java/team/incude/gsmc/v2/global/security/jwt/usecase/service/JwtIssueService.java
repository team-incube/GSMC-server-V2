package team.incude.gsmc.v2.global.security.jwt.usecase.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.member.domain.constant.MemberRole;
import team.incude.gsmc.v2.global.security.jwt.usecase.JwtIssueUseCase;
import team.incude.gsmc.v2.global.security.jwt.persistence.entity.RefreshTokenRedisEntity;
import team.incude.gsmc.v2.global.security.jwt.persistence.repository.RefreshTokenRedisRepository;
import team.incude.gsmc.v2.global.security.jwt.dto.TokenDto;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;

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
