package team.incude.gsmc.v2.domain.evidence.persistence.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RedisHash(value = "readingEvidence")
@Getter
public class ReadingEvidenceRedisEntity {
    @Id
    private UUID id;
    private String title;
    private String author;
    private Integer page;
    private String content;
    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long ttl;

    @Builder
    public ReadingEvidenceRedisEntity(UUID id, String title, String author, Integer page, String content, Long ttl) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.page = page;
        this.content = content;
        this.ttl = ttl;
    }
}
