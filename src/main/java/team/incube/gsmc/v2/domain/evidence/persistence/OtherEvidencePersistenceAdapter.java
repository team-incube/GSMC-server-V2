package team.incube.gsmc.v2.domain.evidence.persistence;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.incube.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.domain.Evidence;
import team.incube.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incube.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incube.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incube.gsmc.v2.domain.evidence.exception.OtherEvidenceNotFoundException;
import team.incube.gsmc.v2.domain.evidence.persistence.entity.EvidenceJpaEntity;
import team.incube.gsmc.v2.domain.evidence.persistence.entity.OtherEvidenceJpaEntity;
import team.incube.gsmc.v2.domain.evidence.persistence.mapper.EvidenceMapper;
import team.incube.gsmc.v2.domain.evidence.persistence.mapper.OtherEvidenceMapper;
import team.incube.gsmc.v2.domain.evidence.persistence.repository.EvidenceJpaRepository;
import team.incube.gsmc.v2.domain.evidence.persistence.repository.OtherEvidenceJpaRepository;
import team.incube.gsmc.v2.global.annotation.PortDirection;
import team.incube.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;
import java.util.Optional;

import static team.incube.gsmc.v2.domain.evidence.persistence.entity.QEvidenceJpaEntity.evidenceJpaEntity;
import static team.incube.gsmc.v2.domain.evidence.persistence.entity.QOtherEvidenceJpaEntity.otherEvidenceJpaEntity;
import static team.incube.gsmc.v2.domain.member.persistence.entity.QMemberJpaEntity.memberJpaEntity;
import static team.incube.gsmc.v2.domain.member.persistence.entity.QStudentDetailJpaEntity.studentDetailJpaEntity;
import static team.incube.gsmc.v2.domain.score.persistence.entity.QScoreJpaEntity.scoreJpaEntity;

/**
 * 기타 증빙자료의 영속성 처리를 담당하는 어댑터 클래스입니다.
 *
 * <p>{@link OtherEvidencePersistencePort}를 구현하며,
 * 저장, 조회, 검색, 삭제 기능을 JPA 및 QueryDSL을 통해 제공합니다.
 *
 * <p>도메인 ↔ 엔티티 간 변환은 {@link OtherEvidenceMapper}를 사용합니다.
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
    private final EvidenceMapper evidenceMapper;
    private final EvidenceJpaRepository evidenceJpaRepository;

    /**
     * 사용자 이메일, 증빙자료 타입을 기준으로 기타 증빙자료를 검색합니다.
     * @param email 사용자 이메일
     * @param type 증빙자료 타입
     * @return 검색된 기타 증빙자료 리스트
     */
    @Override
    public List<OtherEvidence> findOtherEvidenceByMemberEmailAndType(String email, EvidenceType type) {
        return jpaQueryFactory
                .selectFrom(otherEvidenceJpaEntity)
                .join(otherEvidenceJpaEntity.evidence, evidenceJpaEntity)
                .fetchJoin()
                .join(evidenceJpaEntity.score, scoreJpaEntity)
                .fetchJoin()
                .join(scoreJpaEntity.member, memberJpaEntity)
                .fetchJoin()
                .where(
                        memberEmailEq(email),
                        evidenceTypeEq(type)
                ).fetch()
                .stream()
                .map(otherEvidenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<OtherEvidence> findOtherEvidenceByMemberEmailAndTypeAndStatus(String email, EvidenceType type, ReviewStatus status) {
        return jpaQueryFactory
                .selectFrom(otherEvidenceJpaEntity)
                .join(otherEvidenceJpaEntity.evidence, evidenceJpaEntity)
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
                .map(otherEvidenceMapper::toDomain)
                .toList();
    }

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
     * 기타 증빙자료를 저장합니다.
     * @param evidence 저장할 상위 도메인 객체
     * @param otherEvidence 저장할 도메인 객체
     * @return 저장된 도메인 객체
     */
    @Override
    public OtherEvidence saveOtherEvidence(Evidence evidence, OtherEvidence otherEvidence) {
        EvidenceJpaEntity evidenceJpaEntity = evidenceJpaRepository.save(evidenceMapper.toEntity(evidence));

        OtherEvidenceJpaEntity existingEntity = otherEvidenceJpaRepository.findById(otherEvidence.getId().getId())
                .orElse(OtherEvidenceJpaEntity.builder()
                        .evidence(evidenceJpaEntity)
                        .fileUri(otherEvidence.getFileUri())
                        .build());

        OtherEvidenceJpaEntity updatedEntity = OtherEvidenceJpaEntity.builder()
                .id(existingEntity.getId())
                .evidence(existingEntity.getEvidence())
                .fileUri(otherEvidence.getFileUri())
                .build();

        return otherEvidenceMapper.toDomain(otherEvidenceJpaRepository.save(updatedEntity));
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

    @Override
    public OtherEvidence findOtherEvidenceByIdOrNull(Long id) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(otherEvidenceJpaEntity)
                        .where(otherEvidenceJpaEntity.id.eq(id))
                        .fetchOne()
        ).map(otherEvidenceMapper::toDomain).orElse(null);
    }

    private BooleanExpression memberEmailEq(String email) {
        if (email == null) return null;
        return memberJpaEntity.email.eq(email);
    }

    private BooleanExpression evidenceTypeEq(EvidenceType evidenceType) {
        if (evidenceType == null) return null;
        return evidenceJpaEntity.evidenceType.eq(evidenceType);
    }

    private BooleanExpression statusEq(ReviewStatus status) {
        if (status == null) return null;
        return evidenceJpaEntity.reviewStatus.eq(status);
    }

    private BooleanExpression classNumberEq(Integer classNumber) {
        if (classNumber == null) return null;
        return studentDetailJpaEntity.classNumber.eq(classNumber);
    }
}