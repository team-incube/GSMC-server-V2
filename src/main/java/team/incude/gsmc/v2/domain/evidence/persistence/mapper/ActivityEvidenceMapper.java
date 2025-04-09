package team.incude.gsmc.v2.domain.evidence.persistence.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.persistence.entity.ActivityEvidenceJpaEntity;
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
}