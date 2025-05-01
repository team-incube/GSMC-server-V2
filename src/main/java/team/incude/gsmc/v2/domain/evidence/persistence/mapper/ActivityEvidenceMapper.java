package team.incude.gsmc.v2.domain.evidence.persistence.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.DraftActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.persistence.entity.ActivityEvidenceJpaEntity;
import team.incude.gsmc.v2.domain.evidence.persistence.entity.DraftActivityEvidenceRedisEntity;
import team.incude.gsmc.v2.global.mapper.GenericMapper;

@Component
@RequiredArgsConstructor
public class ActivityEvidenceMapper implements GenericMapper<ActivityEvidenceJpaEntity, ActivityEvidence> {

    private final EvidenceMapper evidenceMapper;

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

    @Override
    public ActivityEvidence toDomain(ActivityEvidenceJpaEntity activityEvidenceJpaEntity) {
        return ActivityEvidence.builder()
                .id(evidenceMapper.toDomain(activityEvidenceJpaEntity.getEvidence()))
                .title(activityEvidenceJpaEntity.getTitle())
                .content(activityEvidenceJpaEntity.getContent())
                .imageUrl(activityEvidenceJpaEntity.getImageUri())
                .build();
    }

    public DraftActivityEvidenceRedisEntity toDraftEntity(DraftActivityEvidence draftActivityEvidence) {
        return DraftActivityEvidenceRedisEntity.builder()
                .id(draftActivityEvidence.getId())
                .categoryName(draftActivityEvidence.getCategoryName())
                .title(draftActivityEvidence.getTitle())
                .content(draftActivityEvidence.getContent())
                .imageUrl(draftActivityEvidence.getImageUrl())
                .evidenceType(draftActivityEvidence.getEvidenceType())
                .ttl(draftActivityEvidence.getTtl())
                .build();
    }

    public DraftActivityEvidence toDraftDomain(DraftActivityEvidenceRedisEntity draftActivityEvidenceRedisEntity) {
        return DraftActivityEvidence.builder()
                .id(draftActivityEvidenceRedisEntity.getId())
                .categoryName(draftActivityEvidenceRedisEntity.getCategoryName())
                .title(draftActivityEvidenceRedisEntity.getTitle())
                .content(draftActivityEvidenceRedisEntity.getContent())
                .imageUrl(draftActivityEvidenceRedisEntity.getImageUrl())
                .evidenceType(draftActivityEvidenceRedisEntity.getEvidenceType())
                .ttl(draftActivityEvidenceRedisEntity.getTtl())
                .build();
    }
}