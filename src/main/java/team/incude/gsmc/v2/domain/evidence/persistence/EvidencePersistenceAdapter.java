package team.incude.gsmc.v2.domain.evidence.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.evidence.application.port.EvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.exception.EvidenceNotFoundException;
import team.incude.gsmc.v2.domain.evidence.persistence.mapper.EvidenceMapper;
import team.incude.gsmc.v2.domain.evidence.persistence.repository.EvidenceJpaRepository;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.Optional;

import static team.incude.gsmc.v2.domain.evidence.persistence.entity.QEvidenceJpaEntity.evidenceJpaEntity;

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class EvidencePersistenceAdapter implements EvidencePersistencePort {

    private final EvidenceJpaRepository evidenceJpaRepository;
    private final EvidenceMapper evidenceMapper;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Evidence saveEvidence(Evidence evidence) {
        return evidenceMapper.toDomain(evidenceJpaRepository.save(evidenceMapper.toEntity(evidence)));
    }

    @Override
    public Evidence findEvidenceById(Long id) {
        return evidenceJpaRepository.findById(id)
                .map(evidenceMapper::toDomain)
                .orElseThrow(EvidenceNotFoundException::new);
    }

    @Override
    public void deleteEvidenceById(Long id) {
        evidenceJpaRepository.deleteById(id);
    }

    @Override
    public Evidence findEvidenceByIdWithLock(Long id) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(evidenceJpaEntity)
                        .where(evidenceJpaEntity.id.eq(id))
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .fetchOne()
                ).map(evidenceMapper::toDomain).orElseThrow(EvidenceNotFoundException::new);

    }
}