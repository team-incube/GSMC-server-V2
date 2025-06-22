package team.incube.gsmc.v2.domain.evidence.persistence;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.incube.gsmc.v2.domain.evidence.application.port.ReadingEvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.domain.Evidence;
import team.incube.gsmc.v2.domain.evidence.domain.ReadingEvidence;
import team.incube.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incube.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incube.gsmc.v2.domain.evidence.exception.ReadingEvidenceNotFoundException;
import team.incube.gsmc.v2.domain.evidence.persistence.entity.EvidenceJpaEntity;
import team.incube.gsmc.v2.domain.evidence.persistence.entity.ReadingEvidenceJpaEntity;
import team.incube.gsmc.v2.domain.evidence.persistence.mapper.EvidenceMapper;
import team.incube.gsmc.v2.domain.evidence.persistence.mapper.ReadingEvidenceMapper;
import team.incube.gsmc.v2.domain.evidence.persistence.repository.EvidenceJpaRepository;
import team.incube.gsmc.v2.domain.evidence.persistence.repository.ReadingEvidenceJpaRepository;
import team.incube.gsmc.v2.global.annotation.PortDirection;
import team.incube.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;
import java.util.Optional;

import static team.incube.gsmc.v2.domain.evidence.persistence.entity.QEvidenceJpaEntity.evidenceJpaEntity;
import static team.incube.gsmc.v2.domain.evidence.persistence.entity.QReadingEvidenceJpaEntity.readingEvidenceJpaEntity;
import static team.incube.gsmc.v2.domain.member.persistence.entity.QMemberJpaEntity.memberJpaEntity;
import static team.incube.gsmc.v2.domain.member.persistence.entity.QStudentDetailJpaEntity.studentDetailJpaEntity;
import static team.incube.gsmc.v2.domain.score.persistence.entity.QScoreJpaEntity.scoreJpaEntity;

/**
 * 독서 증빙자료의 영속성 처리를 담당하는 어댑터 클래스입니다.
 *
 * <p>{@link ReadingEvidencePersistencePort}를 구현하며,
 * JPA 및 QueryDSL을 기반으로 독서 증빙자료에 대한 저장, 조회, 검색, 삭제 기능을 제공합니다.
 *
 * <p>도메인 ↔ 엔티티 간 매핑은 {@link ReadingEvidenceMapper}를 통해 수행됩니다.
 * 주요 기능:
 * <ul>
 *     <li>이메일 기반 전체 조회</li>
 *     <li>복합 검색 조건 기반 조회</li>
 *     <li>ID 기반 단건 조회</li>
 *     <li>ID 기반 삭제</li>
 * </ul>
 * @author snowykte0426, suuuuuuminnnnnn
 */
@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class ReadingEvidencePersistenceAdapter implements ReadingEvidencePersistencePort {

    private final ReadingEvidenceJpaRepository readingEvidenceJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final ReadingEvidenceMapper readingEvidenceMapper;
    private final EvidenceMapper evidenceMapper;
    private final EvidenceJpaRepository evidenceJpaRepository;

    /**
     * 사용자 이메일을 기준으로 독서 증빙자료 목록을 조회합니다.
     * @param email 사용자 이메일
     * @return 조회된 독서 증빙자료 도메인 리스트
     */
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

    /**
     * 사용자 이메일과 제목, 증빙자료 타입을 기준으로 독서 증빙자료 목록을 조회합니다.
     * @param email 사용자 이메일
     * @param title 증빙자료 제목
     * @param evidenceType 증빙자료 타입
     * @return 조회된 독서 증빙자료 도메인 리스트
     */
    @Override
    public List<ReadingEvidence> findReadingEvidenceByEmailAndTitleAndType(String email, String title, EvidenceType evidenceType) {
        return jpaQueryFactory
                .selectFrom(readingEvidenceJpaEntity)
                .join(readingEvidenceJpaEntity.evidence, evidenceJpaEntity)
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
                .map(readingEvidenceMapper::toDomain)
                .toList();
    }

    /**
     * 독서 증빙자료를 저장합니다.
     * @param readingEvidence 저장할 독서 증빙자료 도메인 객체
     * @return 저장된 도메인 객체
     */
    @Override
    public ReadingEvidence saveReadingEvidence(ReadingEvidence readingEvidence) {
        return readingEvidenceMapper.toDomain(readingEvidenceJpaRepository.save(readingEvidenceMapper.toEntity(readingEvidence)));
    }

    /**
     * 독서 증빙자료를 저장합니다.
     * @param evidence 저장할 상위 증빙자료 도메인 객체
     * @param readingEvidence 저장할 독서 증빙자료 도메인 객체
     * @return 저장된 도메인 객체
     */
    @Override
    public ReadingEvidence saveReadingEvidence(Evidence evidence, ReadingEvidence readingEvidence) {
        EvidenceJpaEntity evidenceJpaEntity = evidenceJpaRepository.save(evidenceMapper.toEntity(evidence));
        ReadingEvidenceJpaEntity readingEvidenceJpaEntity = ReadingEvidenceJpaEntity.builder()
                .evidence(evidenceJpaEntity)
                .title(readingEvidence.getTitle())
                .author(readingEvidence.getAuthor())
                .content(readingEvidence.getContent())
                .page(readingEvidence.getPage())
                .build();
        return readingEvidenceMapper.toDomain(readingEvidenceJpaRepository.save(readingEvidenceJpaEntity));
    }

    @Override
    public List<ReadingEvidence> findReadingEvidenceByEmailAndStatus(String email, ReviewStatus status) {
        return jpaQueryFactory
                .selectFrom(readingEvidenceJpaEntity)
                .join(readingEvidenceJpaEntity.evidence, evidenceJpaEntity)
                .fetchJoin()
                .join(evidenceJpaEntity.score, scoreJpaEntity)
                .fetchJoin()
                .join(scoreJpaEntity.member, memberJpaEntity)
                .fetchJoin()
                .where(
                        memberEmailEq(email),
                        statusEq(status)
                ).fetch()
                .stream()
                .map(readingEvidenceMapper::toDomain)
                .toList();
    }

    /**
     * ID를 기준으로 독서 증빙자료를 삭제합니다.
     * @param evidenceId 삭제할 증빙자료 ID
     */
    @Override
    public void deleteReadingEvidenceById(Long evidenceId) {
        jpaQueryFactory
                .delete(readingEvidenceJpaEntity)
                .where(readingEvidenceJpaEntity.id.eq(evidenceId))
                .execute();
    }

    /**
     * ID를 기준으로 독서 증빙자료를 조회합니다.
     * @param id 조회할 증빙자료 ID
     * @return 조회된 독서 증빙자료 도메인 객체
     * @throws ReadingEvidenceNotFoundException 존재하지 않을 경우 예외 발생
     */
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
    public ReadingEvidence findReadingEvidenceByIdOrNull(Long id) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(readingEvidenceJpaEntity)
                        .where(readingEvidenceJpaEntity.id.eq(id))
                        .fetchOne()
        ).map(readingEvidenceMapper::toDomain).orElse(null);
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
        return readingEvidenceJpaEntity.title.containsIgnoreCase(title);
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