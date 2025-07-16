package team.incube.gsmc.v2.domain.score.persistence;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import team.incube.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incube.gsmc.v2.domain.score.domain.Score;
import team.incube.gsmc.v2.domain.score.exception.StudentClassMismatchException;
import team.incube.gsmc.v2.domain.score.persistence.mapper.ScoreMapper;
import team.incube.gsmc.v2.domain.score.persistence.repository.ScoreJpaRepository;
import team.incube.gsmc.v2.global.annotation.PortDirection;
import team.incube.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;
import java.util.Optional;

import static team.incube.gsmc.v2.domain.member.persistence.entity.QMemberJpaEntity.memberJpaEntity;
import static team.incube.gsmc.v2.domain.member.persistence.entity.QStudentDetailJpaEntity.studentDetailJpaEntity;
import static team.incube.gsmc.v2.domain.score.persistence.entity.QCategoryJpaEntity.categoryJpaEntity;
import static team.incube.gsmc.v2.domain.score.persistence.entity.QScoreJpaEntity.scoreJpaEntity;

/**
 * 점수 도메인의 영속성 어댑터 구현체입니다.
 * <p>{@link ScorePersistencePort}를 구현하며, JPA 및 QueryDSL을 활용하여 점수 정보를 조회하고 저장합니다.
 * 도메인 객체와 엔티티 간의 변환에는 {@link ScoreMapper}를 사용합니다.
 * <p>제공 기능:
 * <ul>
 *   <li>이메일 또는 학생 코드 기반 점수 단건 조회 (비관적 락 포함)</li>
 *   <li>학생 코드 기반 점수 목록 조회</li>
 *   <li>다중 학생 코드 기반 점수 일괄 조회</li>
 *   <li>점수 저장</li>
 * </ul>
 * @author snowykte0426
 */
@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class ScorePersistenceAdapter implements ScorePersistencePort {

    private final ScoreJpaRepository scoreJpaRepository;
    private final ScoreMapper scoreMapper;
    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 이메일과 카테고리 이름을 기준으로 점수를 조회하고, 비관적 락(PESSIMISTIC_WRITE)을 적용합니다.
     * @param name 카테고리 이름
     * @param email 사용자 이메일
     * @return 조회된 점수 도메인 객체 (없을 경우 null)
     */
    @Override
    public Score findScoreByCategoryNameAndMemberEmailWithLock(String name, String email) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(scoreJpaEntity)
                        .leftJoin(scoreJpaEntity.category, categoryJpaEntity)
                        .on(scoreJpaEntity.category.id.eq(categoryJpaEntity.id))
                        .leftJoin(scoreJpaEntity.member, memberJpaEntity)
                        .on(scoreJpaEntity.member.id.eq(memberJpaEntity.id))
                        .where(scoreJpaEntity.category.name.eq(name)
                                .and(scoreJpaEntity.member.email.eq(email))
                        )
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .fetchOne()
        ).map(scoreMapper::toDomain).orElse(null);
    }

    @Override
    public List<Score> findScoreByMemberEmail(String email) {
        return jpaQueryFactory
                .selectFrom(scoreJpaEntity)
                .join(scoreJpaEntity.member, memberJpaEntity)
                .fetchJoin()
                .join(scoreJpaEntity.category, categoryJpaEntity)
                .fetchJoin()
                .where(memberJpaEntity.email.eq(email))
                .fetch()
                .stream()
                .map(scoreMapper::toDomain)
                .toList();
    }

    /**
     * 여러 학생 코드에 해당하는 점수를 일괄 조회합니다.
     * @param studentCodes 학생 고유 코드 리스트
     * @return 점수 도메인 객체 목록
     */
    @Override
    public List<Score> findScoreByStudentDetailStudentCodes(List<String> studentCodes) {
        return jpaQueryFactory
                .selectFrom(scoreJpaEntity)
                .join(scoreJpaEntity.member, memberJpaEntity)
                .fetchJoin()
                .join(scoreJpaEntity.category, categoryJpaEntity)
                .fetchJoin()
                .join(studentDetailJpaEntity).on(studentDetailJpaEntity.member.eq(memberJpaEntity))
                .where(studentDetailJpaEntity.studentCode.in(studentCodes))
                .fetch()
                .stream()
                .map(scoreMapper::toDomain)
                .toList();
    }

    /**
     * 점수 도메인 객체를 저장합니다.
     * @param score 저장할 점수 도메인 객체
     * @return 저장된 점수 도메인 객체
     */
    @Override
    public Score saveScore(Score score) {
        return scoreMapper.toDomain(scoreJpaRepository.save(scoreMapper.toEntity(score)));
    }

    @Override
    public Integer getStudentHighPercentileByEmailInClass(String email, Integer grade, Integer classNumber) {

        Boolean exists = jpaQueryFactory
                .selectOne()
                .from(studentDetailJpaEntity)
                .join(memberJpaEntity).on(memberJpaEntity.id.eq(studentDetailJpaEntity.member.id))
                .where(
                        memberJpaEntity.email.eq(email),
                        studentDetailJpaEntity.grade.eq(grade),
                        studentDetailJpaEntity.classNumber.eq(classNumber)
                )
                .fetchFirst() != null;

        if (!exists) {
            throw new StudentClassMismatchException();
        }

        NumberTemplate<Double> topPercentile = Expressions.numberTemplate(Double.class,
                "ROUND(100 * (1 - PERCENT_RANK() OVER (PARTITION BY {0}, {1} ORDER BY {2})), 2)",
                studentDetailJpaEntity.grade,
                studentDetailJpaEntity.classNumber,
                studentDetailJpaEntity.totalScore
        );

        Double percentile = jpaQueryFactory
                .select(topPercentile)
                .from(memberJpaEntity)
                .join(studentDetailJpaEntity).on(memberJpaEntity.id.eq(studentDetailJpaEntity.member.id))
                .where(
                        memberJpaEntity.email.eq(email),
                        studentDetailJpaEntity.grade.eq(grade),
                        studentDetailJpaEntity.classNumber.eq(classNumber)
                )
                .fetchOne();

        return percentile != null ? Math.toIntExact(Math.round(percentile)) : 0;
    }

    @Override
    public Integer getStudentLowPercentileByEmailInClass(String email, Integer grade, Integer classNumber) {

        Boolean exists = jpaQueryFactory
                .selectOne()
                .from(studentDetailJpaEntity)
                .join(memberJpaEntity).on(memberJpaEntity.id.eq(studentDetailJpaEntity.member.id))
                .where(
                        memberJpaEntity.email.eq(email),
                        studentDetailJpaEntity.grade.eq(grade),
                        studentDetailJpaEntity.classNumber.eq(classNumber)
                )
                .fetchFirst() != null;

        if (!exists) {
            throw new StudentClassMismatchException();
        }

        NumberTemplate<Double> bottomPercentile = Expressions.numberTemplate(Double.class,
                "ROUND(100 * PERCENT_RANK() OVER (PARTITION BY {0}, {1} ORDER BY {2}), 2)",
                studentDetailJpaEntity.grade,
                studentDetailJpaEntity.classNumber,
                studentDetailJpaEntity.totalScore
        );

        Double percentile = jpaQueryFactory
                .select(bottomPercentile)
                .from(memberJpaEntity)
                .join(studentDetailJpaEntity).on(memberJpaEntity.id.eq(studentDetailJpaEntity.member.id))
                .where(
                        memberJpaEntity.email.eq(email),
                        studentDetailJpaEntity.grade.eq(grade),
                        studentDetailJpaEntity.classNumber.eq(classNumber)
                )
                .fetchOne();

        return percentile != null ? Math.toIntExact(Math.round(percentile)) : 0;
    }

    @Override
    public Integer getStudentHighPercentileByEmailInGrade(String email, Integer grade) {

        Boolean exists = jpaQueryFactory
                .selectOne()
                .from(studentDetailJpaEntity)
                .join(memberJpaEntity).on(memberJpaEntity.id.eq(studentDetailJpaEntity.member.id))
                .where(
                        memberJpaEntity.email.eq(email),
                        studentDetailJpaEntity.grade.eq(grade)
                )
                .fetchFirst() != null;

        if (!exists) {
            throw new StudentClassMismatchException();
        }

        NumberTemplate<Double> topPercentile = Expressions.numberTemplate(Double.class,
                "ROUND(100 * (1 - PERCENT_RANK() OVER (PARTITION BY {0} ORDER BY {1})), 2)",
                studentDetailJpaEntity.grade,
                studentDetailJpaEntity.totalScore
        );

        Double percentile = jpaQueryFactory
                .select(topPercentile)
                .from(memberJpaEntity)
                .join(studentDetailJpaEntity).on(memberJpaEntity.id.eq(studentDetailJpaEntity.member.id))
                .where(
                        memberJpaEntity.email.eq(email),
                        studentDetailJpaEntity.grade.eq(grade)
                )
                .fetchOne();

        return percentile != null ? Math.toIntExact(Math.round(percentile)) : 0;
    }

    @Override
    public Integer getStudentLowPercentileByEmailInGrade(String email, Integer grade) {

        Boolean exists = jpaQueryFactory
                .selectOne()
                .from(studentDetailJpaEntity)
                .join(memberJpaEntity).on(memberJpaEntity.id.eq(studentDetailJpaEntity.member.id))
                .where(
                        memberJpaEntity.email.eq(email),
                        studentDetailJpaEntity.grade.eq(grade)
                )
                .fetchFirst() != null;

        if (!exists) {
            throw new StudentClassMismatchException();
        }

        NumberTemplate<Double> bottomPercentile = Expressions.numberTemplate(Double.class,
                "ROUND(100 * PERCENT_RANK() OVER (PARTITION BY {0} ORDER BY {1}), 2)",
                studentDetailJpaEntity.grade,
                studentDetailJpaEntity.totalScore
        );

        Double percentile = jpaQueryFactory
                .select(bottomPercentile)
                .from(memberJpaEntity)
                .join(studentDetailJpaEntity).on(memberJpaEntity.id.eq(studentDetailJpaEntity.member.id))
                .where(
                        memberJpaEntity.email.eq(email),
                        studentDetailJpaEntity.grade.eq(grade)
                )
                .fetchOne();

        return percentile != null ? Math.toIntExact(Math.round(percentile)) : 0;
    }
}