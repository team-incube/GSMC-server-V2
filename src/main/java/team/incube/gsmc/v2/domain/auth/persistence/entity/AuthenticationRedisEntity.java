package team.incube.gsmc.v2.domain.auth.persistence.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@RedisHash(value = "authentication")
@Getter
@NoArgsConstructor
public class AuthenticationRedisEntity {
    @Id
    private String email;
    private Integer attemptCount;
    private Boolean verified;
    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long ttl;

    @Builder
    public AuthenticationRedisEntity(String email, Integer attemptCount, Boolean verified, Long ttl) {
        this.email = email;
        this.attemptCount = attemptCount;
        this.verified = verified;
        this.ttl = ttl;
    }
}