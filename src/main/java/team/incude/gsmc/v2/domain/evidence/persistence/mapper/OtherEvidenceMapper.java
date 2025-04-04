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
                .id(evidenceMapper.toEntity(otherEvidence.getId()))
                .fileUrl(otherEvidence.getFileUrl())
                .build();
    }

    @Override
    public OtherEvidence toDomain(OtherEvidenceJpaEntity otherEvidenceJpaEntity) {
        return OtherEvidence.builder()
                .id(evidenceMapper.toDomain(otherEvidenceJpaEntity.getId()))
                .fileUrl(otherEvidenceJpaEntity.getFileUrl())
                .build();
    }
}