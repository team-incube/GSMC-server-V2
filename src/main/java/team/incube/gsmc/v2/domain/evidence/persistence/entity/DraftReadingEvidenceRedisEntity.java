package team.incube.gsmc.v2.domain.evidence.persistence.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RedisHash(value = "draft_reading_evidence")
@Getter
public class DraftReadingEvidenceRedisEntity {
    @Id
    private UUID id;
    private String title;
    private String author;
    private Integer page;
    private String content;
    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long ttl;
    @Indexed
    private String email;

    @Builder
    public DraftReadingEvidenceRedisEntity(UUID id, String title, String author, Integer page, String content, Long ttl, String email) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.page = page;
        this.content = content;
        this.ttl = ttl;
        this.email = email;
    }
}
