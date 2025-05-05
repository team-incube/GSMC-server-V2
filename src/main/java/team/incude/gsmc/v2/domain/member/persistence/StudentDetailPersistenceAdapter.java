package team.incude.gsmc.v2.domain.member.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.StudentDetail;
import team.incude.gsmc.v2.domain.member.domain.StudentDetailWithEvidence;
import team.incude.gsmc.v2.domain.member.exception.MemberInvalidException;
import team.incude.gsmc.v2.domain.member.exception.MemberNotFoundException;
import team.incude.gsmc.v2.domain.member.persistence.mapper.StudentDetailMapper;
import team.incude.gsmc.v2.domain.member.persistence.projection.StudentProjection;
import team.incude.gsmc.v2.domain.member.persistence.repository.StudentDetailJpaRepository;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;
import java.util.Optional;

import static team.incude.gsmc.v2.domain.evidence.persistence.entity.QEvidenceJpaEntity.evidenceJpaEntity;
import static team.incude.gsmc.v2.domain.member.persistence.entity.QStudentDetailJpaEntity.studentDetailJpaEntity;

/**
 * 학생 상세 정보에 대한 영속성 접근을 제공하는 Adapter 클래스입니다.
 * <p>{@link StudentDetailPersistencePort}를 구현하며, QueryDSL과 JPA를 통해 학생 정보 및 증빙 상태를 조회, 저장합니다.
 * {@link StudentDetailMapper}를 통해 도메인 객체와 엔티티 간 매핑을 수행합니다.
 * <p>제공 기능:
 * <ul>
 *   <li>학생 코드 또는 이메일 기반 단건 조회</li>
 *   <li>락을 포함한 조회</li>
 *   <li>학급별 조회</li>
 *   <li>증빙자료 포함 학생 조회</li>
 *   <li>검색 조건 기반 페이징</li>
 *   <li>총점 조회 및 저장</li>
 * </ul>
 * @author snowykte0426
 */
@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class StudentDetailPersistenceAdapter implements StudentDetailPersistencePort {

    private final StudentDetailJpaRepository studentDetailJpaRepository;
    private final StudentDetailMapper studentDetailMapper;
    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 학생 코드를 기준으로 학생 상세 정보를 조회합니다.
     * @param studentCode 학생 고유 코드
     * @return 학생 상세 도메인 객체
     * @throws MemberInvalidException 학생이 존재하지 않을 경우
     */
    @Override
    public StudentDetail findStudentDetailByStudentCode(String studentCode) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(studentDetailJpaEntity)
                        .where(studentDetailJpaEntity.studentCode.eq(studentCode))
                        .fetchOne()
        ).map(studentDetailMapper::toDomain).orElseThrow(MemberInvalidException::new);
    }

    /**
     * 학생 코드를 기준으로 비관적 락을 걸고 학생 정보를 조회합니다.
     * @param studentCode 학생 고유 코드
     * @return 락이 걸린 학생 상세 도메인 객체
     * @throws MemberInvalidException 학생이 존재하지 않을 경우
     */
    @Override
    public StudentDetail findStudentDetailByStudentCodeWithLock(String studentCode) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(studentDetailJpaEntity)
                        .where(studentDetailJpaEntity.studentCode.eq(studentCode))
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .fetchOne()
        ).map(studentDetailMapper::toDomain).orElseThrow(MemberInvalidException::new);
    }

    /**
     * 이메일을 기준으로 학생 상세 정보를 조회합니다.
     * @param email 회원 이메일
     * @return 학생 상세 도메인 객체
     * @throws MemberInvalidException 학생이 존재하지 않을 경우
     */
    @Override
    public StudentDetail findStudentDetailByMemberEmail(String email) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(studentDetailJpaEntity)
                        .join(studentDetailJpaEntity.member)
                        .fetchJoin()
                        .where(studentDetailJpaEntity.member.email.eq(email))
                        .fetchOne()
        ).map(studentDetailMapper::toDomain).orElseThrow(MemberInvalidException::new);
    }

    /**
     * 주어진 학년과 반 번호에 해당하는 학생 목록을 조회합니다.
     * @param grade 학년
     * @param classNumber 반 번호
     * @return 학생 상세 도메인 객체 리스트
     */
    @Override
    public List<StudentDetail> findStudentDetailByGradeAndClassNumber(Integer grade, Integer classNumber) {
        return jpaQueryFactory
                .selectFrom(studentDetailJpaEntity)
                .where(studentDetailJpaEntity.grade.eq(grade)
                        .and(studentDetailJpaEntity.classNumber.eq(classNumber)))
                .fetch()
                .stream()
                .map(studentDetailMapper::toDomain)
                .toList();
    }

    /**
     * 학생 코드를 기준으로 증빙 포함 학생 정보를 조회합니다.
     * @param studentCode 학생 고유 코드
     * @return 증빙 포함 학생 상세 정보
     * @throws MemberNotFoundException 학생이 존재하지 않을 경우
     */
    @Override
    public StudentDetailWithEvidence findStudentDetailWithEvidenceByStudentCode(String studentCode) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                                Projections.constructor(
                                        StudentProjection.class,
                                        studentDetailJpaEntity.member.email,
                                        studentDetailJpaEntity.member.name,
                                        studentDetailJpaEntity.grade,
                                        studentDetailJpaEntity.classNumber,
                                        studentDetailJpaEntity.number,
                                        studentDetailJpaEntity.totalScore,
                                        evidenceJpaEntity.id.isNotNull(),
                                        studentDetailJpaEntity.member.role
                                )
                        )
                        .from(studentDetailJpaEntity)
                        .leftJoin(evidenceJpaEntity)
                        .on(evidenceJpaEntity.score.member.id.eq(studentDetailJpaEntity.member.id))
                        .where(studentDetailJpaEntity.studentCode.eq(studentCode))
                        .fetchOne()
        ).map(studentDetailMapper::fromProjection).orElseThrow(MemberNotFoundException::new);
    }

    /**
     * 이메일을 기준으로 증빙자료 포함 학생 정보를 조회합니다.
     * @param email 회원 이메일
     * @return 증빙 포함 학생 상세 정보
     * @throws MemberNotFoundException 학생이 존재하지 않을 경우
     */
    @Override
    public StudentDetailWithEvidence findStudentDetailWithEvidenceByMemberEmail(String email) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                                Projections.constructor(
                                        StudentProjection.class,
                                        studentDetailJpaEntity.member.email,
                                        studentDetailJpaEntity.member.name,
                                        studentDetailJpaEntity.grade,
                                        studentDetailJpaEntity.classNumber,
                                        studentDetailJpaEntity.number,
                                        studentDetailJpaEntity.totalScore,
                                        evidenceJpaEntity.id.isNotNull(),
                                        studentDetailJpaEntity.member.role
                                )
                        )
                        .from(studentDetailJpaEntity)
                        .leftJoin(evidenceJpaEntity)
                        .on(evidenceJpaEntity.score.member.id.eq(studentDetailJpaEntity.member.id))
                        .where(studentDetailJpaEntity.member.email.eq(email))
                        .fetchOne()
        ).map(studentDetailMapper::fromProjection).orElseThrow(MemberNotFoundException::new);
    }

    /**
     * 증빙자료 검토 상태가 존재하는 사용자 정보를 가진 학생 목록을 조회합니다.
     * @return 증빙 포함 학생 상세 정보 리스트
     */
    @Override
    public List<StudentDetailWithEvidence> findStudentDetailWithEvidenceReviewStatusNotNullMember() {
        return jpaQueryFactory
                .select(
                        Projections.constructor(
                                StudentProjection.class,
                                studentDetailJpaEntity.member.email,
                                studentDetailJpaEntity.member.name,
                                studentDetailJpaEntity.grade,
                                studentDetailJpaEntity.classNumber,
                                studentDetailJpaEntity.number,
                                studentDetailJpaEntity.totalScore,
                                evidenceJpaEntity.id.isNotNull(),
                                studentDetailJpaEntity.member.role
                        )
                )
                .from(studentDetailJpaEntity)
                .leftJoin(evidenceJpaEntity)
                .on(
                        evidenceJpaEntity.score.member.id.eq(studentDetailJpaEntity.member.id)
                                .and(evidenceJpaEntity.reviewStatus.eq(ReviewStatus.PENDING))
                )
                .where(studentDetailJpaEntity.member.isNotNull())
                .orderBy(studentDetailJpaEntity.studentCode.asc())
                .fetch()
                .stream()
                .map(studentDetailMapper::fromProjection)
                .toList();
    }

    /**
     * 검토 상태가 존재하는 학생 중 검색 조건에 부합하는 목록을 페이징하여 조회합니다.
     * @param name 학생 이름 필터
     * @param grade 학년 필터
     * @param classNumber 반 번호 필터
     * @param pageable 페이징 정보
     * @return 페이징된 학생 상세 정보 목록
     */
    @Override
    public Page<StudentDetailWithEvidence> searchStudentDetailWithEvidenceReviewStatusNotNullMember(String name, Integer grade, Integer classNumber, Pageable pageable) {
        List<StudentProjection> content = jpaQueryFactory
                .select(
                        Projections.constructor(
                                StudentProjection.class,
                                studentDetailJpaEntity.member.email,
                                studentDetailJpaEntity.member.name,
                                studentDetailJpaEntity.grade,
                                studentDetailJpaEntity.classNumber,
                                studentDetailJpaEntity.number,
                                studentDetailJpaEntity.totalScore,
                                evidenceJpaEntity.id.isNotNull(),
                                studentDetailJpaEntity.member.role
                        )
                )
                .from(studentDetailJpaEntity)
                .leftJoin(evidenceJpaEntity)
                .on(
                        evidenceJpaEntity.score.member.id.eq(studentDetailJpaEntity.member.id)
                                .and(evidenceJpaEntity.reviewStatus.eq(ReviewStatus.PENDING))
                )
                .where(
                        studentDetailJpaEntity.member.isNotNull(),
                        name != null ? studentDetailJpaEntity.member.name.contains(name) : null,
                        grade != null ? studentDetailJpaEntity.grade.eq(grade) : null,
                        classNumber != null ? studentDetailJpaEntity.classNumber.eq(classNumber) : null
                )
                .orderBy(studentDetailJpaEntity.studentCode.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = jpaQueryFactory
                .select(studentDetailJpaEntity.count())
                .from(studentDetailJpaEntity)
                .leftJoin(evidenceJpaEntity)
                .on(
                        evidenceJpaEntity.score.member.id.eq(studentDetailJpaEntity.member.id)
                                .and(evidenceJpaEntity.reviewStatus.eq(ReviewStatus.PENDING))
                )
                .where(
                        studentDetailJpaEntity.member.isNotNull(),
                        name != null ? studentDetailJpaEntity.member.name.contains(name) : null,
                        grade != null ? studentDetailJpaEntity.grade.eq(grade) : null,
                        classNumber != null ? studentDetailJpaEntity.classNumber.eq(classNumber) : null
                )
                .fetchOne();
        List<StudentDetailWithEvidence> result = content.stream()
                .map(studentDetailMapper::fromProjection)
                .toList();
        return new PageImpl<>(result, pageable, total != null ? total : 0);
    }

    /**
     * 학생 코드로 총합 점수를 조회합니다.
     * @param studentCode 학생 고유 코드
     * @return 총합 점수
     * @throws MemberInvalidException 학생이 존재하지 않을 경우
     */
    @Override
    public Integer findTotalScoreByStudentCode(String studentCode) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(studentDetailJpaEntity.totalScore)
                        .from(studentDetailJpaEntity)
                        .where(studentDetailJpaEntity.studentCode.eq(studentCode))
                        .fetchOne()
        ).orElseThrow(MemberInvalidException::new);
    }

    /**
     * 학생 상세 도메인 객체를 저장합니다.
     * @param studentDetail 저장할 도메인 객체
     * @return 저장된 학생 상세 도메인 객체
     */
    @Override
    public StudentDetail saveStudentDetail(StudentDetail studentDetail) {
        return studentDetailMapper.toDomain(studentDetailJpaRepository.save(studentDetailMapper.toEntity(studentDetail)));
    }
}