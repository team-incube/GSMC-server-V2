package team.incude.gsmc.v2.domain.evidence.persistence;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.evidence.exception.ActivityEvidenceNotFountException;
import team.incude.gsmc.v2.domain.evidence.persistence.mapper.ActivityEvidenceMapper;
import team.incude.gsmc.v2.domain.evidence.persistence.repository.ActivityEvidenceJpaRepository;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;

import static team.incude.gsmc.v2.domain.evidence.persistence.entity.QActivityEvidenceJpaEntity.activityEvidenceJpaEntity;
import static team.incude.gsmc.v2.domain.evidence.persistence.entity.QEvidenceJpaEntity.evidenceJpaEntity;
import static team.incude.gsmc.v2.domain.member.persistence.entity.QMemberJpaEntity.memberJpaEntity;
import static team.incude.gsmc.v2.domain.member.persistence.entity.QStudentDetailJpaEntity.studentDetailJpaEntity;
import static team.incude.gsmc.v2.domain.score.persistence.entity.QCategoryJpaEntity.categoryJpaEntity;
import static team.incude.gsmc.v2.domain.score.persistence.entity.QScoreJpaEntity.scoreJpaEntity;

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class ActivityEvidencePersistenceAdapter implements ActivityEvidencePersistencePort {

    private final ActivityEvidenceJpaRepository activityEvidenceJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final ActivityEvidenceMapper activityEvidenceMapper;

    @Override
    public List<ActivityEvidence> findActivityEvidenceByEmailAndEvidenceType(String email, EvidenceType evidenceType) {
        return jpaQueryFactory
                .selectFrom(activityEvidenceJpaEntity)
                .join(activityEvidenceJpaEntity.evidence, evidenceJpaEntity).fetchJoin()
                .join(evidenceJpaEntity.score, scoreJpaEntity).fetchJoin()
                .join(scoreJpaEntity.member, memberJpaEntity).fetchJoin()
                .join(scoreJpaEntity.category, categoryJpaEntity).fetchJoin()
                .where(
                        memberEmailEq(email),
                        evidenceTypeEq(evidenceType)
                )
                .fetch()
                .stream()
                .map(activityEvidenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<ActivityEvidence> findActivityEvidenceByStudentCodeAndTypeAndTitleAndStatusAndGradeAndClassNumber(String studentCode, EvidenceType evidenceType, String title, ReviewStatus status, Integer grade, Integer classNumber) {
        return jpaQueryFactory
                .selectFrom(activityEvidenceJpaEntity)
                .join(activityEvidenceJpaEntity.evidence, evidenceJpaEntity).fetchJoin()
                .join(evidenceJpaEntity.score, scoreJpaEntity).fetchJoin()
                .join(studentDetailJpaEntity).on(studentDetailJpaEntity.studentCode.eq(studentCode)).fetchJoin()
                .where(
                        studentCodeEq(studentCode),
                        evidenceTypeEq(evidenceType),
                        titleEq(title),
                        statusEq(status),
                        gradeEq(grade),
                        classNumberEq(classNumber)
                )
                .fetch()
                .stream()
                .map(activityEvidenceMapper::toDomain)
                .toList();
    }

    @Override
    public ActivityEvidence saveActivityEvidence(ActivityEvidence activityEvidence) {
        return activityEvidenceMapper.toDomain(activityEvidenceJpaRepository.save(activityEvidenceMapper.toEntity(activityEvidence)));
    }

    @Override
    public void deleteActivityEvidenceById(Long evidenceId) {
        long deletedCount = jpaQueryFactory
                .delete(activityEvidenceJpaEntity)
                .where(activityEvidenceJpaEntity.id.eq(evidenceId))
                .execute();

        if (deletedCount == 0) {
            throw new ActivityEvidenceNotFountException();
        }
    }

    @Override
    public Boolean existsActivityEvidenceByEvidenceId(Long evidenceId) {
        Integer result = jpaQueryFactory
                .selectOne()
                .from(activityEvidenceJpaEntity)
                .where(activityEvidenceJpaEntity.id.eq(evidenceId))
                .fetchFirst();

        return result != null;
    }

    private BooleanExpression memberEmailEq(String email) {
        if (email == null) return null;
        return memberJpaEntity.email.eq(email);
    }

    private BooleanExpression studentCodeEq(Integer studentCode) {
        if (studentCode == null) return null;
        return studentDetailJpaEntity.studentCode.eq(studentCode);
    }

    private BooleanExpression evidenceTypeEq(EvidenceType evidenceType) {
        if (evidenceType == null) return null;
        return evidenceJpaEntity.evidenceType.eq(evidenceType);
    }

    private BooleanExpression titleEq(String title) {
        if (title == null || title.isBlank()) return null;
        return activityEvidenceJpaEntity.title.eq(title);
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