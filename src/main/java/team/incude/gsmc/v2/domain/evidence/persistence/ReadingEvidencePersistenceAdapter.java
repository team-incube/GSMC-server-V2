package team.incude.gsmc.v2.domain.evidence.persistence;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.evidence.application.port.ReadingEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.domain.ReadingEvidence;
import team.incude.gsmc.v2.domain.evidence.exception.EvidenceNotFoundException;
import team.incude.gsmc.v2.domain.evidence.persistence.mapper.ReadingEvidenceMapper;
import team.incude.gsmc.v2.domain.evidence.persistence.repository.ReadingEvidenceJpaRepository;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;
import java.util.Optional;

import static team.incude.gsmc.v2.domain.evidence.persistence.entity.QEvidenceJpaEntity.evidenceJpaEntity;
import static team.incude.gsmc.v2.domain.evidence.persistence.entity.QReadingEvidenceJpaEntity.readingEvidenceJpaEntity;
import static team.incude.gsmc.v2.domain.member.persistence.entity.QMemberJpaEntity.memberJpaEntity;
import static team.incude.gsmc.v2.domain.score.persistence.entity.QCategoryJpaEntity.categoryJpaEntity;
import static team.incude.gsmc.v2.domain.score.persistence.entity.QScoreJpaEntity.scoreJpaEntity;

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class ReadingEvidencePersistenceAdapter implements ReadingEvidencePersistencePort {

    private final ReadingEvidenceJpaRepository readingEvidenceJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final ReadingEvidenceMapper readingEvidenceMapper;

    @Override
    public List<ReadingEvidence> findReadingEvidenceByEmail(String email) {
        return jpaQueryFactory
                .selectFrom(readingEvidenceJpaEntity)
                .leftJoin(readingEvidenceJpaEntity.evidence, evidenceJpaEntity).fetchJoin()
                .leftJoin(evidenceJpaEntity.score, scoreJpaEntity).fetchJoin()
                .leftJoin(scoreJpaEntity.member, memberJpaEntity).fetchJoin()
                .where(memberEmailEq(email))
                .fetch()
                .stream()
                .map(readingEvidenceMapper::toDomain)
                .toList();
    }

    @Override
    public ReadingEvidence findReadingEvidenceByEvidenceId(Long evidenceId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(readingEvidenceJpaEntity)
                        .leftJoin(readingEvidenceJpaEntity.evidence, evidenceJpaEntity).fetchJoin()
                        .where(evidenceJpaEntity.id.eq(evidenceId))
                        .fetchOne()
        ).map(readingEvidenceMapper::toDomain).orElseThrow(EvidenceNotFoundException::new);
    }

    @Override
    public ReadingEvidence saveReadingEvidence(ReadingEvidence readingEvidence) {
        return readingEvidenceMapper.toDomain(readingEvidenceJpaRepository.save(readingEvidenceMapper.toEntity(readingEvidence)));
    }

    @Override
    public List<ReadingEvidence> findReadingEvidenceByEmailAndTitle(String email, String title) {
        return jpaQueryFactory
                .selectFrom(readingEvidenceJpaEntity)
                .leftJoin(readingEvidenceJpaEntity.evidence, evidenceJpaEntity).fetchJoin()
                .leftJoin(evidenceJpaEntity.score, scoreJpaEntity).fetchJoin()
                .leftJoin(scoreJpaEntity.member, memberJpaEntity).fetchJoin()
                .leftJoin(scoreJpaEntity.category, categoryJpaEntity).fetchJoin()
                .where(
                        memberEmailEq(email),
                        readingEvidenceJpaEntity.title.eq(title)
                )
                .fetch()
                .stream()
                .map(readingEvidenceMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteReadingEvidenceById(Long evidenceId) {
        jpaQueryFactory
                .delete(readingEvidenceJpaEntity)
                .where(readingEvidenceJpaEntity.id.eq(evidenceId))
                .execute();
    }

    @Override
    public Boolean existsReadingEvidenceByEvidenceId(Long evidenceId) {
        Integer result = jpaQueryFactory
                .selectOne()
                .from(readingEvidenceJpaEntity)
                .leftJoin(readingEvidenceJpaEntity.evidence, evidenceJpaEntity).fetchJoin()
                .where(evidenceJpaEntity.id.eq(evidenceId))
                .fetchFirst();
        return result != null;
    }

    private BooleanExpression memberEmailEq(String email) {
        if (email == null) return null;
        return memberJpaEntity.email.eq(email);
    }
}