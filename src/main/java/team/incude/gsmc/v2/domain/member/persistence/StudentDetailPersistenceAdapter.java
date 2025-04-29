package team.incude.gsmc.v2.domain.member.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
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

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class StudentDetailPersistenceAdapter implements StudentDetailPersistencePort {

    private final StudentDetailJpaRepository studentDetailJpaRepository;
    private final StudentDetailMapper studentDetailMapper;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public StudentDetail findStudentDetailByStudentCode(String studentCode) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(studentDetailJpaEntity)
                        .where(studentDetailJpaEntity.studentCode.eq(studentCode))
                        .fetchOne()
        ).map(studentDetailMapper::toDomain).orElseThrow(MemberInvalidException::new);
    }

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

    @Override
    public Page<StudentDetailWithEvidence> searchStudentDetailWithEvidenceReiewStatusNotNullMember(String name, Integer grade, Integer classNumber, Pageable pageable) {
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

    @Override
    @Deprecated(forRemoval = true, since = "StudentCode로 전환으로 인한 메서드 제거 예정")
    public Integer findTotalScoreByMemberEmail(String email) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(studentDetailJpaEntity.totalScore)
                        .from(studentDetailJpaEntity)
                        .where(studentDetailJpaEntity.member.email.eq(email))
                        .fetchOne()
        ).orElseThrow(MemberInvalidException::new);
    }

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

    @Override
    public StudentDetail saveStudentDetail(StudentDetail studentDetail) {
        return studentDetailMapper.toDomain(studentDetailJpaRepository.save(studentDetailMapper.toEntity(studentDetail)));
    }
}