package team.incude.gsmc.v2.domain.evidence.persistence;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.evidence.application.port.ReadingEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.domain.DraftReadingEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.ReadingEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.evidence.exception.DraftReadingEvidenceNotFoundException;
import team.incude.gsmc.v2.domain.evidence.exception.ReadingEvidenceNotFoundException;
import team.incude.gsmc.v2.domain.evidence.persistence.mapper.ReadingEvidenceMapper;
import team.incude.gsmc.v2.domain.evidence.persistence.repository.DraftReadingEvidenceRedisRepository;
import team.incude.gsmc.v2.domain.evidence.persistence.repository.ReadingEvidenceJpaRepository;
import team.incude.gsmc.v2.domain.member.persistence.mapper.MemberMapper;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    private final MemberMapper memberMapper;
    private final DraftReadingEvidenceRedisRepository draftReadingEvidenceRedisRepository;

    @Override
    public List<ReadingEvidence> findReadingEvidenceByEmail(String email) {
        return jpaQueryFactory
                .selectFrom(readingEvidenceJpaEntity)
                .join(readingEvidenceJpaEntity.evidence, evidenceJpaEntity)
                .fetchJoin()
                .join(evidenceJpaEntity.score, scoreJpaEntity)
                .fetchJoin()
                .join(scoreJpaEntity.member, memberJpaEntity)
                .fetchJoin()
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
    public List<ReadingEvidence> searchReadingEvidence(String studentCode, String title, EvidenceType evidenceType, ReviewStatus status, Integer grade, Integer classNumber) {
        return jpaQueryFactory
                .selectFrom(readingEvidenceJpaEntity)
                .join(readingEvidenceJpaEntity.evidence, evidenceJpaEntity)
                .fetchJoin()
                .join(evidenceJpaEntity.score, scoreJpaEntity)
                .fetchJoin()
                .join(scoreJpaEntity.member, memberJpaEntity)
                .fetchJoin()
                .join(studentDetailJpaEntity)
                .on(studentDetailJpaEntity.member.eq(memberJpaEntity)).fetchJoin()
                .where(
                        studentCodeEq(studentCode),
                        titleEq(title),
                        evidenceTypeEq(evidenceType),
                        statusEq(status),
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
    public ReadingEvidence findReadingEvidenceById(Long id) {
        return Optional.ofNullable(
                        jpaQueryFactory
                                .selectFrom(readingEvidenceJpaEntity)
                                .where(readingEvidenceJpaEntity.id.eq(id))
                                .fetchOne()
                ).map(readingEvidenceMapper::toDomain).orElseThrow(ReadingEvidenceNotFoundException::new);
    }

    @Override
    public void deleteDraftReadingEvidenceById(UUID draftId) {
        draftReadingEvidenceRedisRepository.deleteById(draftId);
    }

    @Override
    public DraftReadingEvidence saveDraftReadingEvidence(DraftReadingEvidence draftReadingEvidence) {
        return readingEvidenceMapper.toDraftDomain(draftReadingEvidenceRedisRepository.save(readingEvidenceMapper.toDraftEntity(draftReadingEvidence)));
    }

    @Override
    public DraftReadingEvidence findDraftReadingEvidenceById(UUID draftId) {
        return draftReadingEvidenceRedisRepository.findById(draftId)
                .map(readingEvidenceMapper::toDraftDomain)
                .orElseThrow(DraftReadingEvidenceNotFoundException::new);
    }

    private BooleanExpression memberEmailEq(String email) {
        if (email == null) return null;
        return memberJpaEntity.email.eq(email);
    }

    private BooleanExpression studentCodeEq(String studentCode) {
        if (studentCode == null) return null;
        return studentDetailJpaEntity.studentCode.eq(studentCode);
    }

    private BooleanExpression titleEq(String title) {
        if (title == null) return null;
        return readingEvidenceJpaEntity.title.eq(title);
    }

    private BooleanExpression evidenceTypeEq(EvidenceType evidenceType) {
        if (evidenceType == null) return null;
        return evidenceJpaEntity.evidenceType.eq(evidenceType);
    }

    private BooleanExpression statusEq(ReviewStatus status) {
        if (status == null) return null;
        return evidenceJpaEntity.reviewStatus.eq(status);
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