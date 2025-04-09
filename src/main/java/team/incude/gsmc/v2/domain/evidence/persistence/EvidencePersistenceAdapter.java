package team.incude.gsmc.v2.domain.evidence.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.evidence.application.port.EvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.persistence.mapper.EvidenceMapper;
import team.incude.gsmc.v2.domain.evidence.persistence.repository.EvidenceJpaRepository;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class EvidencePersistenceAdapter implements EvidencePersistencePort {

    private final EvidenceJpaRepository evidenceJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final EvidenceMapper evidenceMapper;

    @Override
    public Evidence findEvidenceById(Long id) {
        return evidenceJpaRepository.findById(id)
                .map(evidenceMapper::toDomain)
                .orElse(null);
    }

    @Override
    public Evidence saveEvidence(Evidence evidence) {
        return evidenceMapper.toDomain(evidenceJpaRepository.saveAndFlush(evidenceMapper.toEntity(evidence)));
    }
}