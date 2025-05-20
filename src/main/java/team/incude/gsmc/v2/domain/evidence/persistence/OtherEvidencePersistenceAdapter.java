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

/**
 * 기타 증빙자료의 영속성 처리를 담당하는 어댑터 클래스입니다.
 *
 * <p>{@link team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort}를 구현하며,
 * 저장, 조회, 검색, 삭제 기능을 JPA 및 QueryDSL을 통해 제공합니다.
 *
 * <p>도메인 ↔ 엔티티 간 변환은 {@link team.incude.gsmc.v2.domain.evidence.persistence.mapper.OtherEvidenceMapper}를 사용합니다.
 *
 * 주요 기능:
 * <ul>
 *     <li>기타 증빙자료 저장 및 삭제</li>
 *     <li>이메일 기반 전체 조회</li>
 *     <li>복합 조건 검색</li>
 *     <li>ID 기반 단건 조회</li>
 * </ul>
 *
 * @author suuuuuuminnnnnn
 */
@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class OtherEvidencePersistenceAdapter implements OtherEvidencePersistencePort {

    private final OtherEvidenceJpaRepository otherEvidenceJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final OtherEvidenceMapper otherEvidenceMapper;

    /**
     * 기타 증빙자료를 저장합니다.
     * @param otherEvidence 저장할 도메인 객체
     * @return 저장된 도메인 객체
     */
    @Override
    public OtherEvidence saveOtherEvidence(OtherEvidence otherEvidence) {
        return otherEvidenceMapper.toDomain(otherEvidenceJpaRepository.save(otherEvidenceMapper.toEntity(otherEvidence)));
    }

    /**
     * 학생 코드, 증빙자료 타입, 검토 상태, 학년, 반을 기준으로 기타 증빙자료를 검색합니다.
     * @param studentCode 학번
     * @param evidenceType 증빙자료 타입
     * @param status 검토 상태
     * @param grade 학년
     * @param classNumber 반
     * @return 검색된 기타 증빙자료 리스트
     */
    @Override
    public List<OtherEvidence> searchOtherEvidence(String studentCode, EvidenceType evidenceType, ReviewStatus status, Integer grade, Integer classNumber) {
        return jpaQueryFactory
                .selectFrom(otherEvidenceJpaEntity)
                .join(otherEvidenceJpaEntity.evidence, evidenceJpaEntity)
                .fetchJoin()
                .join(evidenceJpaEntity.score, scoreJpaEntity)
                .fetchJoin()
                .join(scoreJpaEntity.member, memberJpaEntity)
                .fetchJoin()
                .join(studentDetailJpaEntity)
                .on(studentDetailJpaEntity.member.eq(memberJpaEntity))
                .fetchJoin()
                .join(scoreJpaEntity.category, categoryJpaEntity)
                .fetchJoin()
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

    /**
     * 사용자 이메일을 기준으로 기타 증빙자료 목록을 조회합니다.
     * @param email 사용자 이메일
     * @return 조회된 기타 증빙자료 리스트
     */
    @Override
    public List<OtherEvidence> findOtherEvidenceByEmail(String email) {
        return jpaQueryFactory
                .selectFrom(otherEvidenceJpaEntity)
                .join(otherEvidenceJpaEntity.evidence, evidenceJpaEntity)
                .fetchJoin()
                .join(evidenceJpaEntity.score, scoreJpaEntity)
                .fetchJoin()
                .join(scoreJpaEntity.member, memberJpaEntity)
                .fetchJoin()
                .where(memberEmailEq(email))
                .fetch()
                .stream()
                .map(otherEvidenceMapper::toDomain)
                .toList();
    }

    /**
     * 증빙자료 ID를 기준으로 기타 증빙자료를 삭제합니다.
     * @param evidenceId 삭제할 증빙자료 ID
     */
    @Override
    public void deleteOtherEvidenceById(Long evidenceId) {
        otherEvidenceJpaRepository.deleteById(evidenceId);
    }

    /**
     * 기타 증빙자료 ID로 단건 조회합니다.
     * @param id 조회할 증빙자료 ID
     * @return 조회된 기타 증빙자료
     * @throws OtherEvidenceNotFoundException 존재하지 않을 경우 예외 발생
     */
    @Override
    public OtherEvidence findOtherEvidenceById(Long id) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(otherEvidenceJpaEntity)
                        .where(otherEvidenceJpaEntity.id.eq(id))
                        .fetchOne()
                ).map(otherEvidenceMapper::toDomain).orElseThrow(OtherEvidenceNotFoundException::new);
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