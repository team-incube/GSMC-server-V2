package team.incude.gsmc.v2.global.security.jwt.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.concurrent.TimeUnit;

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
