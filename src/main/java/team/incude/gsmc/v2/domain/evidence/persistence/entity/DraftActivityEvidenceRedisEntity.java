package team.incude.gsmc.v2.domain.evidence.persistence.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RedisHash(value = "activityEvidence")
@Getter
public class DraftActivityEvidenceRedisEntity {
    @Id
    private UUID id;
    private String categoryName;
    private String title;
    private String content;
    private String imageUrl;
    private EvidenceType evidenceType;
    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long ttl;
    @Indexed
    private String email;

    @Builder
    public DraftActivityEvidenceRedisEntity(UUID id, String categoryName, String title, String content, String imageUrl, EvidenceType evidenceType, Long ttl, String email) {
        this.id = id;
        this.categoryName = categoryName;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.evidenceType = evidenceType;
        this.ttl = ttl;
        this.email = email;
    }
}
