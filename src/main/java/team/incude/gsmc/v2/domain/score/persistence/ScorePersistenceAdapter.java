package team.incude.gsmc.v2.domain.score.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.domain.score.persistence.mapper.ScoreMapper;
import team.incude.gsmc.v2.domain.score.persistence.repository.ScoreJpaRepository;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;
import java.util.Optional;

import static team.incude.gsmc.v2.domain.member.persistence.entity.QMemberJpaEntity.memberJpaEntity;
import static team.incude.gsmc.v2.domain.score.persistence.entity.QCategoryJpaEntity.categoryJpaEntity;
import static team.incude.gsmc.v2.domain.score.persistence.entity.QScoreJpaEntity.scoreJpaEntity;

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class ScorePersistenceAdapter implements ScorePersistencePort {

    private final ScoreJpaRepository scoreJpaRepository;
    private final ScoreMapper scoreMapper;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Score findScoreByCategoryNameAndMemberEmail(String name, String email) {
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
                        .fetchOne()
        ).map(scoreMapper::toDomain).orElse(null);
    }

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

    @Override
    public Score saveScore(Score score) {
        return scoreMapper.toDomain(scoreJpaRepository.save(scoreMapper.toEntity(score)));
    }
}