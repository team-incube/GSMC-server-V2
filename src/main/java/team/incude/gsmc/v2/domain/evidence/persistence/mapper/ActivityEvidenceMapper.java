package team.incude.gsmc.v2.domain.evidence.persistence.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.DraftActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.persistence.entity.ActivityEvidenceJpaEntity;
import team.incude.gsmc.v2.domain.evidence.persistence.entity.DraftActivityEvidenceRedisEntity;
import team.incude.gsmc.v2.global.mapper.GenericMapper;

/**
 * 활동 증빙자료에 대한 도메인 ↔ JPA 및 Redis 엔티티 매핑을 담당하는 매퍼 클래스입니다.
 * <p>{@link ActivityEvidenceJpaEntity}, {@link DraftActivityEvidenceRedisEntity} 와
 * 도메인 객체 {@link ActivityEvidence}, {@link DraftActivityEvidence} 간의 상호 변환을 수행합니다.
 * <p>공통 증빙정보는 {@link EvidenceMapper}를 통해 매핑됩니다.
 * @author suuuuuuminnnnnn
 */
@Component
@RequiredArgsConstructor
public class ActivityEvidenceMapper implements GenericMapper<ActivityEvidenceJpaEntity, ActivityEvidence> {

    private final EvidenceMapper evidenceMapper;

    /**
     * 도메인 객체 {@link ActivityEvidence}를 JPA 엔티티 {@link ActivityEvidenceJpaEntity}로 변환합니다.
     * @param activityEvidence 변환할 도메인 객체
     * @return 변환된 JPA 엔티티
     */
    @Override
    public ActivityEvidenceJpaEntity toEntity(ActivityEvidence activityEvidence) {
        return ActivityEvidenceJpaEntity.builder()
                .id(activityEvidence.getId().getId())
                .evidence(evidenceMapper.toEntity(activityEvidence.getId()))
                .title(activityEvidence.getTitle())
                .content(activityEvidence.getContent())
                .imageUri(activityEvidence.getImageUrl())
                .build();
    }

    /**
     * JPA 엔티티 {@link ActivityEvidenceJpaEntity}를 도메인 객체 {@link ActivityEvidence}로 변환합니다.
     * @param activityEvidenceJpaEntity 변환할 JPA 엔티티
     * @return 변환된 도메인 객체
     */
    @Override
    public ActivityEvidence toDomain(ActivityEvidenceJpaEntity activityEvidenceJpaEntity) {
        return ActivityEvidence.builder()
                .id(evidenceMapper.toDomain(activityEvidenceJpaEntity.getEvidence()))
                .title(activityEvidenceJpaEntity.getTitle())
                .content(activityEvidenceJpaEntity.getContent())
                .imageUrl(activityEvidenceJpaEntity.getImageUri())
                .build();
    }

    /**
     * 도메인 객체 {@link DraftActivityEvidence}를 Redis 엔티티 {@link DraftActivityEvidenceRedisEntity}로 변환합니다.
     * @param draftActivityEvidence 변환할 도메인 객체
     * @return 변환된 Redis 엔티티
     */
    public DraftActivityEvidenceRedisEntity toDraftEntity(DraftActivityEvidence draftActivityEvidence) {
        return DraftActivityEvidenceRedisEntity.builder()
                .id(draftActivityEvidence.getId())
                .categoryName(draftActivityEvidence.getCategoryName())
                .title(draftActivityEvidence.getTitle())
                .content(draftActivityEvidence.getContent())
                .imageUrl(draftActivityEvidence.getImageUrl())
                .evidenceType(draftActivityEvidence.getEvidenceType())
                .ttl(draftActivityEvidence.getTtl())
                .email(draftActivityEvidence.getEmail())
                .build();
    }

    /**
     * Redis 엔티티 {@link DraftActivityEvidenceRedisEntity}를 도메인 객체 {@link DraftActivityEvidence}로 변환합니다.
     * @param draftActivityEvidenceRedisEntity 변환할 Redis 엔티티
     * @return 변환된 도메인 객체
     */
    public DraftActivityEvidence toDraftDomain(DraftActivityEvidenceRedisEntity draftActivityEvidenceRedisEntity) {
        return DraftActivityEvidence.builder()
                .id(draftActivityEvidenceRedisEntity.getId())
                .categoryName(draftActivityEvidenceRedisEntity.getCategoryName())
                .title(draftActivityEvidenceRedisEntity.getTitle())
                .content(draftActivityEvidenceRedisEntity.getContent())
                .imageUrl(draftActivityEvidenceRedisEntity.getImageUrl())
                .evidenceType(draftActivityEvidenceRedisEntity.getEvidenceType())
                .ttl(draftActivityEvidenceRedisEntity.getTtl())
                .email(draftActivityEvidenceRedisEntity.getEmail())
                .build();
    }
}