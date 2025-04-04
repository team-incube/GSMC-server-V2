package team.incude.gsmc.v2.domain.evidence.persistence.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.persistence.entity.EvidenceJpaEntity;
import team.incude.gsmc.v2.domain.score.persistence.mapper.ScoreMapper;
import team.incude.gsmc.v2.global.mapper.GenericMapper;

@Component
@RequiredArgsConstructor
public class EvidenceMapper implements GenericMapper<EvidenceJpaEntity, Evidence> {

    private final ScoreMapper scoreMapper;

    @Override
    public EvidenceJpaEntity toEntity(Evidence evidence) {
        return EvidenceJpaEntity.builder()
                .id(evidence.getId())
                .score(scoreMapper.toEntity(evidence.getScore()))
                .evidenceType(evidence.getEvidenceType())
                .reviewStatus(evidence.getReviewStatus())
                .createdAt(evidence.getCreatedAt())
                .updatedAt(evidence.getUpdatedAt())
                .build();
    }

    @Override
    public Evidence toDomain(EvidenceJpaEntity evidenceJpaEntity) {
        return Evidence.builder()
                .id(evidenceJpaEntity.getId())
                .score(scoreMapper.toDomain(evidenceJpaEntity.getScore()))
                .evidenceType(evidenceJpaEntity.getEvidenceType())
                .reviewStatus(evidenceJpaEntity.getReviewStatus())
                .createdAt(evidenceJpaEntity.getCreatedAt())
                .updatedAt(evidenceJpaEntity.getUpdatedAt())
                .build();
    }
}