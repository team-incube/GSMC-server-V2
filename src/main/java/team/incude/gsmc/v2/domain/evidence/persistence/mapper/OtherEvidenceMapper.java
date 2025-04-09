package team.incude.gsmc.v2.domain.evidence.persistence.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.evidence.persistence.entity.OtherEvidenceJpaEntity;
import team.incude.gsmc.v2.global.mapper.GenericMapper;

@Component
@RequiredArgsConstructor
public class OtherEvidenceMapper implements GenericMapper<OtherEvidenceJpaEntity, OtherEvidence> {

    private final EvidenceMapper evidenceMapper;

    @Override
    public OtherEvidenceJpaEntity toEntity(OtherEvidence otherEvidence) {
        return OtherEvidenceJpaEntity.builder()
                .id(otherEvidence.getId().getId())
                .evidence(evidenceMapper.toEntity(otherEvidence.getId()))
                .fileUri(otherEvidence.getFileUri())
                .build();
    }

    @Override
    public OtherEvidence toDomain(OtherEvidenceJpaEntity otherEvidenceJpaEntity) {
        return OtherEvidence.builder()
                .id(evidenceMapper.toDomain(otherEvidenceJpaEntity.getEvidence()))
                .fileUri(otherEvidenceJpaEntity.getFileUri())
                .build();
    }
}