package team.incude.gsmc.v2.domain.evidence.persistence;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.evidence.application.port.ReadingEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.domain.ReadingEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.persistence.mapper.ReadingEvidenceMapper;
import team.incude.gsmc.v2.domain.evidence.persistence.repository.ReadingEvidenceJpaRepository;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;

import static team.incude.gsmc.v2.domain.evidence.persistence.entity.QEvidenceJpaEntity.evidenceJpaEntity;
import static team.incude.gsmc.v2.domain.evidence.persistence.entity.QReadingEvidenceJpaEntity.readingEvidenceJpaEntity;
import static team.incude.gsmc.v2.domain.member.persistence.entity.QMemberJpaEntity.memberJpaEntity;
import static team.incude.gsmc.v2.domain.member.persistence.entity.QStudentDetailJpaEntity.studentDetailJpaEntity;
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
    public ReadingEvidence saveReadingEvidence(ReadingEvidence readingEvidence) {
        return readingEvidenceMapper.toDomain(readingEvidenceJpaRepository.save(readingEvidenceMapper.toEntity(readingEvidence)));
    }

    @Override
    public List<ReadingEvidence> findReadingEvidenceByEmailAndTitleAndTypeAndGradeAndClassNumber(String email, String title, EvidenceType evidenceType, Integer grade, Integer classNumber) {
        return jpaQueryFactory
                .selectFrom(readingEvidenceJpaEntity)
                .leftJoin(readingEvidenceJpaEntity.evidence, evidenceJpaEntity).fetchJoin()
                .leftJoin(evidenceJpaEntity.score, scoreJpaEntity).fetchJoin()
                .leftJoin(scoreJpaEntity.member, memberJpaEntity).fetchJoin()
                .join(studentDetailJpaEntity).on(studentDetailJpaEntity.member.id.eq(memberJpaEntity.id)).fetchJoin()
                .where(
                        memberEmailEq(email),
                        titleEq(title),
                        evidenceTypeEq(evidenceType),
                        gradeEq(grade),
                        classNumberEq(classNumber)
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

    private BooleanExpression titleEq(String title) {
        if (title == null) return null;
        return readingEvidenceJpaEntity.title.eq(title);
    }

    private BooleanExpression evidenceTypeEq(EvidenceType evidenceType) {
        if (evidenceType == null) return null;
        return evidenceJpaEntity.evidenceType.eq(evidenceType);
    }

    private BooleanExpression gradeEq(Integer grade) {
        if (grade == null) return null;
        return studentDetailJpaEntity.grade.eq(grade);
    }

    private BooleanExpression classNumberEq(Integer classNumber) {
        if (classNumber == null) return null;
        return studentDetailJpaEntity.classNumber.eq(classNumber);
    }
}