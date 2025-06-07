package team.incude.gsmc.v2.domain.evidence.persistence;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.evidence.exception.ActivityEvidenceNotFountException;
import team.incude.gsmc.v2.domain.evidence.persistence.entity.ActivityEvidenceJpaEntity;
import team.incude.gsmc.v2.domain.evidence.persistence.entity.EvidenceJpaEntity;
import team.incude.gsmc.v2.domain.evidence.persistence.mapper.ActivityEvidenceMapper;
import team.incude.gsmc.v2.domain.evidence.persistence.mapper.EvidenceMapper;
import team.incude.gsmc.v2.domain.evidence.persistence.repository.ActivityEvidenceJpaRepository;
import team.incude.gsmc.v2.domain.evidence.persistence.repository.EvidenceJpaRepository;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;
import java.util.Optional;

import static team.incude.gsmc.v2.domain.evidence.persistence.entity.QActivityEvidenceJpaEntity.activityEvidenceJpaEntity;
import static team.incude.gsmc.v2.domain.evidence.persistence.entity.QEvidenceJpaEntity.evidenceJpaEntity;
import static team.incude.gsmc.v2.domain.member.persistence.entity.QMemberJpaEntity.memberJpaEntity;
import static team.incude.gsmc.v2.domain.member.persistence.entity.QStudentDetailJpaEntity.studentDetailJpaEntity;
import static team.incude.gsmc.v2.domain.score.persistence.entity.QCategoryJpaEntity.categoryJpaEntity;
import static team.incude.gsmc.v2.domain.score.persistence.entity.QScoreJpaEntity.scoreJpaEntity;

/**
 * 활동 증빙자료의 영속성 처리를 담당하는 어댑터 클래스입니다.
 * <p>{@link team.incude.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort}를 구현하며,
 * 활동 증빙자료에 대한 저장, 조회, 검색, 삭제 기능을 JPA 및 QueryDSL 기반으로 제공합니다.
 * <p>도메인 객체 ↔ JPA 엔티티 간 매핑은 {@link team.incude.gsmc.v2.domain.evidence.persistence.mapper.ActivityEvidenceMapper}를 통해 수행됩니다.
 * 주요 기능:
 * <ul>
 *     <li>사용자 이메일 및 증빙자료 타입 기반 조회</li>
 *     <li>학번, 제목, 상태, 학년, 반 조건 기반 복합 검색</li>
 *     <li>활동 증빙자료 저장 및 삭제</li>
 *     <li>단건 조회</li>
 * </ul>
 * @author snowykte0426, suuuuuuminnnnnn
 */
@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class ActivityEvidencePersistenceAdapter implements ActivityEvidencePersistencePort {

    private final ActivityEvidenceJpaRepository activityEvidenceJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final ActivityEvidenceMapper activityEvidenceMapper;
    private final EvidenceJpaRepository evidenceJpaRepository;
    private final EvidenceMapper evidenceMapper;

    /**
     * 사용자 이메일과 증빙자료 타입을 기준으로 활동 증빙자료 목록을 조회합니다.
     * @param email 사용자 이메일
     * @param evidenceType 활동 증빙자료 타입
     * @return 조회된 활동 증빙자료 리스트
     */
    @Override
    public List<ActivityEvidence> findActivityEvidenceByEmailAndEvidenceType(String email, EvidenceType evidenceType) {
        return jpaQueryFactory
                .selectFrom(activityEvidenceJpaEntity)
                .join(activityEvidenceJpaEntity.evidence, evidenceJpaEntity)
                .fetchJoin()
                .join(evidenceJpaEntity.score, scoreJpaEntity)
                .fetchJoin()
                .join(scoreJpaEntity.member, memberJpaEntity)
                .fetchJoin()
                .join(scoreJpaEntity.category, categoryJpaEntity)
                .fetchJoin()
                .where(
                        memberEmailEq(email),
                        evidenceTypeEq(evidenceType)
                )
                .fetch()
                .stream()
                .map(activityEvidenceMapper::toDomain)
                .toList();
    }

    /**
     * 사용자 이메일과 제목과 증빙자료 타입을 기준으로 활동 증빙자료 목록을 조회합니다.
     * @param email 사용자 이메일
     * @param title 증빙자료 제목
     * @param evidenceType 활동 증빙자료 타입
     * @return 조회된 활동 증빙자료 리스트
     */
    @Override
    public List<ActivityEvidence> findActivityEvidenceByMemberEmailAndTitleAndType(String email, String title, EvidenceType evidenceType) {
        return jpaQueryFactory
                .selectFrom(activityEvidenceJpaEntity)
                .join(activityEvidenceJpaEntity.evidence, evidenceJpaEntity)
                .fetchJoin()
                .join(evidenceJpaEntity.score, scoreJpaEntity)
                .fetchJoin()
                .join(scoreJpaEntity.member, memberJpaEntity)
                .fetchJoin()
                .join(studentDetailJpaEntity).on(studentDetailJpaEntity.member.eq(memberJpaEntity))
                .fetchJoin()
                .where(
                        memberEmailEq(email),
                        titleEq(title),
                        evidenceTypeEq(evidenceType)
                ).fetch()
                .stream()
                .map(activityEvidenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<ActivityEvidence> findActivityEvidenceByMemberEmailAndTypeAndStatus(String email, EvidenceType type, ReviewStatus status) {
        return jpaQueryFactory
                .selectFrom(activityEvidenceJpaEntity)
                .join(activityEvidenceJpaEntity.evidence, evidenceJpaEntity)
                .fetchJoin()
                .join(evidenceJpaEntity.score, scoreJpaEntity)
                .fetchJoin()
                .join(scoreJpaEntity.member, memberJpaEntity)
                .fetchJoin()
                .where(
                        memberEmailEq(email),
                        evidenceTypeEq(type),
                        statusEq(status)
                ).fetch()
                .stream()
                .map(activityEvidenceMapper::toDomain)
                .toList();
    }

    /**
     * 사용자 이메일을 기준으로 활동 증빙자료 목록을 조회합니다.
     * @param email 사용자 이메일
     */
    @Override
    public List<ActivityEvidence> findActivityEvidenceByMemberEmail(String email) {
        return jpaQueryFactory
                .selectFrom(activityEvidenceJpaEntity)
                .join(activityEvidenceJpaEntity.evidence, evidenceJpaEntity)
                .fetchJoin()
                .join(evidenceJpaEntity.score, scoreJpaEntity)
                .fetchJoin()
                .join(scoreJpaEntity.member, memberJpaEntity)
                .fetchJoin()
                .where(
                        memberEmailEq(email)
                ).fetch()
                .stream()
                .map(activityEvidenceMapper::toDomain)
                .toList();
    }

    /**
     * 활동 증빙자료를 저장합니다.
     * @param activityEvidence 저장할 활동 증빙자료 도메인 객체
     * @return 저장된 활동 증빙자료 도메인 객체
     */
    @Override
    public ActivityEvidence saveActivityEvidence(ActivityEvidence activityEvidence) {
        return activityEvidenceMapper.toDomain(activityEvidenceJpaRepository.save(activityEvidenceMapper.toEntity(activityEvidence)));
    }

    /**
     * 활동 증빙자료를 저장합니다.
     * @param evidence 저장할 상위 증빙자료 도메인 객체
     * @param activityEvidence 저장할 활동 증빙자료 도메인 객체
     * @return 저장된 활동 증빙자료 도메인 객체
     */
    @Override
    public ActivityEvidence saveActivityEvidence(Evidence evidence, ActivityEvidence activityEvidence) {
        EvidenceJpaEntity evidenceJpaEntity = evidenceJpaRepository.save(evidenceMapper.toEntity(evidence));
        ActivityEvidenceJpaEntity activityEvidenceJpaEntity = ActivityEvidenceJpaEntity.builder()
                .evidence(evidenceJpaEntity)
                .content(activityEvidence.getContent())
                .imageUri(activityEvidence.getImageUrl())
                .title(activityEvidence.getTitle())
                .build();
        return activityEvidenceMapper.toDomain(activityEvidenceJpaRepository.save(activityEvidenceJpaEntity));
    }

    /**
     * 활동 증빙자료 ID를 기반으로 해당 자료를 삭제합니다.
     * @param evidenceId 삭제할 증빙자료의 ID
     * @throws ActivityEvidenceNotFountException 삭제 대상이 존재하지 않을 경우
     */
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


    /**
     * 활동 증빙자료 ID를 기준으로 단건 조회합니다.
     * @param id 조회할 증빙자료 ID
     * @return 조회된 활동 증빙자료 도메인 객체
     * @throws ActivityEvidenceNotFountException 존재하지 않을 경우 예외 발생
     */
    @Override
    public ActivityEvidence findActivityEvidenceById(Long id) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(activityEvidenceJpaEntity)
                        .where(activityEvidenceJpaEntity.id.eq(id))
                        .fetchOne()
                ).map(activityEvidenceMapper::toDomain).orElseThrow(ActivityEvidenceNotFountException::new);

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

    private BooleanExpression titleEq(String title) {
        if (title == null || title.isBlank()) return null;
        return activityEvidenceJpaEntity.title.containsIgnoreCase(title.trim());
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