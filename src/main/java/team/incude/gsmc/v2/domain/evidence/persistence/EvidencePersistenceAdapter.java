package team.incude.gsmc.v2.domain.evidence.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.evidence.application.port.EvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.exception.EvidenceNotFoundException;
import team.incude.gsmc.v2.domain.evidence.persistence.mapper.EvidenceMapper;
import team.incude.gsmc.v2.domain.evidence.persistence.repository.EvidenceJpaRepository;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;

import static team.incude.gsmc.v2.domain.evidence.persistence.entity.QEvidenceJpaEntity.evidenceJpaEntity;
import static team.incude.gsmc.v2.domain.member.persistence.entity.QMemberJpaEntity.memberJpaEntity;
import static team.incude.gsmc.v2.domain.score.persistence.entity.QScoreJpaEntity.scoreJpaEntity;

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class EvidencePersistenceAdapter implements EvidencePersistencePort {

    private final EvidenceJpaRepository evidenceJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final EvidenceMapper evidenceMapper;

    @Override
    public Evidence saveEvidence(Evidence evidence) {
        return evidenceMapper.toDomain(evidenceJpaRepository.save(evidenceMapper.toEntity(evidence)));
    }

    @Override
    public List<Evidence> findEvidencesByEmail(String email) {
        return jpaQueryFactory
                .selectFrom(evidenceJpaEntity)
                .leftJoin(evidenceJpaEntity.score, scoreJpaEntity).fetchJoin()
                .leftJoin(scoreJpaEntity.member, memberJpaEntity).fetchJoin()
                .where(
                        memberJpaEntity.email.eq(email)
                )
                .fetch()
                .stream()
                .map(evidenceMapper::toDomain)
                .toList();
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
}