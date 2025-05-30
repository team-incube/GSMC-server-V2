package team.incude.gsmc.v2.domain.evidence.persistence;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.evidence.application.port.ReadingEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.domain.ReadingEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.evidence.exception.ReadingEvidenceNotFoundException;
import team.incude.gsmc.v2.domain.evidence.persistence.mapper.ReadingEvidenceMapper;
import team.incude.gsmc.v2.domain.evidence.persistence.repository.ReadingEvidenceJpaRepository;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;
import java.util.Optional;

import static team.incude.gsmc.v2.domain.evidence.persistence.entity.QEvidenceJpaEntity.evidenceJpaEntity;
import static team.incude.gsmc.v2.domain.evidence.persistence.entity.QReadingEvidenceJpaEntity.readingEvidenceJpaEntity;
import static team.incude.gsmc.v2.domain.member.persistence.entity.QMemberJpaEntity.memberJpaEntity;
import static team.incude.gsmc.v2.domain.member.persistence.entity.QStudentDetailJpaEntity.studentDetailJpaEntity;
import static team.incude.gsmc.v2.domain.score.persistence.entity.QScoreJpaEntity.scoreJpaEntity;

/**
 * 독서 증빙자료의 영속성 처리를 담당하는 어댑터 클래스입니다.
 *
 * <p>{@link team.incude.gsmc.v2.domain.evidence.application.port.ReadingEvidencePersistencePort}를 구현하며,
 * JPA 및 QueryDSL을 기반으로 독서 증빙자료에 대한 저장, 조회, 검색, 삭제 기능을 제공합니다.
 *
 * <p>도메인 ↔ 엔티티 간 매핑은 {@link team.incude.gsmc.v2.domain.evidence.persistence.mapper.ReadingEvidenceMapper}를 통해 수행됩니다.
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
     * 독서 증빙자료를 저장합니다.
     * @param readingEvidence 저장할 독서 증빙자료 도메인 객체
     * @return 저장된 도메인 객체
     */
    @Override
    public ReadingEvidence saveReadingEvidence(ReadingEvidence readingEvidence) {
        return readingEvidenceMapper.toDomain(readingEvidenceJpaRepository.save(readingEvidenceMapper.toEntity(readingEvidence)));
    }

    /**
     * 학생 정보, 제목, 증빙자료 타입, 검토 상태 등을 기준으로 독서 증빙자료를 검색합니다.
     * @param studentCode 학번
     * @param title 제목
     * @param evidenceType 증빙자료 타입
     * @param status 검토 상태
     * @param grade 학년
     * @param classNumber 반
     * @return 조건에 부합하는 독서 증빙자료 리스트
     */
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