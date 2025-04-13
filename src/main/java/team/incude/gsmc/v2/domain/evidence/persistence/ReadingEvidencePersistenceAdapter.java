package team.incude.gsmc.v2.domain.evidence.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.evidence.application.port.ReadingEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.domain.ReadingEvidence;
import team.incude.gsmc.v2.domain.evidence.exception.EvidenceNotFoundException;
import team.incude.gsmc.v2.domain.evidence.persistence.mapper.ReadingEvidenceMapper;
import team.incude.gsmc.v2.domain.evidence.persistence.repository.ReadingEvidenceJpaRepository;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.member.persistence.mapper.MemberMapper;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;
import java.util.Optional;

import static team.incude.gsmc.v2.domain.evidence.persistence.entity.QEvidenceJpaEntity.evidenceJpaEntity;
import static team.incude.gsmc.v2.domain.evidence.persistence.entity.QReadingEvidenceJpaEntity.readingEvidenceJpaEntity;
import static team.incude.gsmc.v2.domain.score.persistence.entity.QScoreJpaEntity.scoreJpaEntity;

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class ReadingEvidencePersistenceAdapter implements ReadingEvidencePersistencePort {

    private final ReadingEvidenceJpaRepository readingEvidenceJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final ReadingEvidenceMapper readingEvidenceMapper;
    private final MemberMapper memberMapper;

    @Override
    public List<ReadingEvidence> findReadingEvidenceByMember(Member member) {
        return jpaQueryFactory
                .selectFrom(readingEvidenceJpaEntity)
                .leftJoin(readingEvidenceJpaEntity.evidence, evidenceJpaEntity).fetchJoin()
                .leftJoin(evidenceJpaEntity.score, scoreJpaEntity).fetchJoin()
                .where(scoreJpaEntity.member.eq(memberMapper.toEntity(member)))
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
    public List<ReadingEvidence> findReadingEvidenceByMemberAndTypeAndTitle(Member member) {
        return List.of();
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
}