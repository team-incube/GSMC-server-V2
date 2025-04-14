package team.incude.gsmc.v2.domain.evidence.persistence;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.evidence.exception.EvidenceNotFoundException;
import team.incude.gsmc.v2.domain.evidence.persistence.mapper.OtherEvidenceMapper;
import team.incude.gsmc.v2.domain.evidence.persistence.repository.OtherEvidenceJpaRepository;
import team.incude.gsmc.v2.domain.member.persistence.mapper.MemberMapper;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;
import java.util.Optional;

import static team.incude.gsmc.v2.domain.evidence.persistence.entity.QEvidenceJpaEntity.evidenceJpaEntity;
import static team.incude.gsmc.v2.domain.evidence.persistence.entity.QOtherEvidenceJpaEntity.otherEvidenceJpaEntity;
import static team.incude.gsmc.v2.domain.member.persistence.entity.QMemberJpaEntity.memberJpaEntity;
import static team.incude.gsmc.v2.domain.score.persistence.entity.QCategoryJpaEntity.categoryJpaEntity;
import static team.incude.gsmc.v2.domain.score.persistence.entity.QScoreJpaEntity.scoreJpaEntity;

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class OtherEvidencePersistenceAdapter implements OtherEvidencePersistencePort {

    private final OtherEvidenceJpaRepository otherEvidenceJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final OtherEvidenceMapper otherEvidenceMapper;
    private final MemberMapper memberMapper;

    @Override
    public OtherEvidence saveOtherEvidence(OtherEvidence otherEvidence) {
        return otherEvidenceMapper.toDomain(otherEvidenceJpaRepository.save(otherEvidenceMapper.toEntity(otherEvidence)));
    }

    @Override
    public List<OtherEvidence> findOtherEvidenceByEmail(String email) {
        return jpaQueryFactory
                .selectFrom(otherEvidenceJpaEntity)
                .leftJoin(otherEvidenceJpaEntity.evidence, evidenceJpaEntity).fetchJoin()
                .leftJoin(evidenceJpaEntity.score, scoreJpaEntity).fetchJoin()
                .leftJoin(scoreJpaEntity.member, memberJpaEntity).fetchJoin()
                .leftJoin(scoreJpaEntity.category, categoryJpaEntity).fetchJoin()
                .where(memberEmailEq(email))
                .fetch()
                .stream()
                .map(otherEvidenceMapper::toDomain)
                .toList();
    }

    @Override
    public OtherEvidence findOtherEvidenceById(Long evidenceId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(otherEvidenceJpaEntity)
                .leftJoin(otherEvidenceJpaEntity.evidence, evidenceJpaEntity).fetchJoin()
                .where(evidenceJpaEntity.id.eq(evidenceId))
                .fetchOne()
        ).map(otherEvidenceMapper::toDomain).orElseThrow(EvidenceNotFoundException::new);
    }

    @Override
    public void deleteOtherEvidenceById(Long evidenceId) {
        otherEvidenceJpaRepository.deleteById(evidenceId);
    }

    @Override
    public Boolean existsOtherEvidenceByEvidenceId(Long evidenceId) {
        Integer result = jpaQueryFactory
                .selectOne()
                .from(otherEvidenceJpaEntity)
                .where(otherEvidenceJpaEntity.id.eq(evidenceId))
                .fetchOne();
        return result != null;
    }

    private BooleanExpression memberEmailEq(String email) {
        if (email == null) return null;
        return memberJpaEntity.email.eq(email);
    }
}