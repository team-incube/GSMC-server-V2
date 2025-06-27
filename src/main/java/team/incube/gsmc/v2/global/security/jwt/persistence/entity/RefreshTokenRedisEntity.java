package team.incube.gsmc.v2.global.security.jwt.persistence.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.concurrent.TimeUnit;

/**
 * Redis에 저장되는 Refresh Token 엔티티 클래스입니다.
 * <p>이 클래스는 Redis에 저장될 Refresh Token의 구조를 정의하며, 토큰, 이메일, 만료 시간을 포함합니다.
 * <p>토큰은 고유 식별자로 사용되며, 이메일은 해당 토큰과 연관된 사용자 식별을 위해 사용됩니다.
 * 만료 시간은 토큰의 유효 기간을 나타내며, Redis에서 자동으로 삭제되도록 설정됩니다.
 * @author jihoonwjj
 */
@RedisHash("refresh_token")
@Getter
@NoArgsConstructor
public class RefreshTokenRedisEntity {
    @Id
    private String token;
    @Indexed
    private String email;
    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long expiration;

    @Builder
    public RefreshTokenRedisEntity(String token, String email, Long expiration) {
        this.token = token;
        this.email = email;
        this.expiration = expiration;
    }
}
