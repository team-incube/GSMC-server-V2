package team.incude.gsmc.v2.domain.evidence.persistence;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.evidence.exception.OtherEvidenceNotFoundException;
import team.incude.gsmc.v2.domain.evidence.persistence.mapper.OtherEvidenceMapper;
import team.incude.gsmc.v2.domain.evidence.persistence.repository.OtherEvidenceJpaRepository;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;
import java.util.Optional;

import static team.incude.gsmc.v2.domain.evidence.persistence.entity.QEvidenceJpaEntity.evidenceJpaEntity;
import static team.incude.gsmc.v2.domain.evidence.persistence.entity.QOtherEvidenceJpaEntity.otherEvidenceJpaEntity;
import static team.incude.gsmc.v2.domain.member.persistence.entity.QMemberJpaEntity.memberJpaEntity;
import static team.incude.gsmc.v2.domain.member.persistence.entity.QStudentDetailJpaEntity.studentDetailJpaEntity;
import static team.incude.gsmc.v2.domain.score.persistence.entity.QCategoryJpaEntity.categoryJpaEntity;
import static team.incude.gsmc.v2.domain.score.persistence.entity.QScoreJpaEntity.scoreJpaEntity;

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class OtherEvidencePersistenceAdapter implements OtherEvidencePersistencePort {

    private final OtherEvidenceJpaRepository otherEvidenceJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final OtherEvidenceMapper otherEvidenceMapper;

    @Override
    public OtherEvidence saveOtherEvidence(OtherEvidence otherEvidence) {
        return otherEvidenceMapper.toDomain(otherEvidenceJpaRepository.save(otherEvidenceMapper.toEntity(otherEvidence)));
    }

    @Override
    public List<OtherEvidence> findOtherEvidenceByStudentCodeAndTypeAndStatusAndGradeAndClassNumber(String studentCode, EvidenceType evidenceType, ReviewStatus status, Integer grade, Integer classNumber) {
        return jpaQueryFactory
                .selectFrom(otherEvidenceJpaEntity)
                .join(otherEvidenceJpaEntity.evidence, evidenceJpaEntity).fetchJoin()
                .join(evidenceJpaEntity.score, scoreJpaEntity).fetchJoin()
                .join(studentDetailJpaEntity).on(studentDetailJpaEntity.studentCode.eq(studentCode)).fetchJoin()
                .join(studentDetailJpaEntity.member, memberJpaEntity).fetchJoin()
                .join(scoreJpaEntity.category, categoryJpaEntity).fetchJoin()
                .where(
                        studentCodeEq(studentCode),
                        evidenceTypeEq(evidenceType),
                        statusEq(status),
                        gradeEq(grade),
                        classNumberEq(classNumber)
                ).fetch()
                .stream()
                .map(otherEvidenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<OtherEvidence> findOtherEvidenceByEmail(String email) {
        return jpaQueryFactory
                .selectFrom(otherEvidenceJpaEntity)
                .join(otherEvidenceJpaEntity.evidence, evidenceJpaEntity).fetchJoin()
                .join(evidenceJpaEntity.score, scoreJpaEntity).fetchJoin()
                .where(memberEmailEq(email))
                .fetch()
                .stream()
                .map(otherEvidenceMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteOtherEvidenceById(Long evidenceId) {
        otherEvidenceJpaRepository.deleteById(evidenceId);
    }

    @Override
    public OtherEvidence findOtherEvidenceById(Long id) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(otherEvidenceJpaEntity)
                        .where(otherEvidenceJpaEntity.id.eq(id))
                        .fetchOne())
                .map(otherEvidenceMapper::toDomain)
                .orElseThrow(OtherEvidenceNotFoundException::new);
    }

    private BooleanExpression memberEmailEq(String email) {
        if (email == null) return null;
        return memberJpaEntity.email.eq(email);
    }

    private BooleanExpression studentCodeEq(String studentCode) {
        if (studentCode == null) return null;
        return studentDetailJpaEntity.studentCode.eq(studentCode);
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