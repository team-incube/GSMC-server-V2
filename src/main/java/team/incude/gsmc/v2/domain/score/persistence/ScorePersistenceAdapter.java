package team.incude.gsmc.v2.domain.score.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.score.application.port.CategoryPersistencePort;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.domain.score.persistence.mapper.ScoreMapper;
import team.incude.gsmc.v2.domain.score.persistence.repository.ScoreJpaRepository;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.Optional;

import static team.incude.gsmc.v2.domain.score.persistence.entity.QScoreJpaEntity.scoreJpaEntity;

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class ScorePersistenceAdapter implements ScorePersistencePort {

    private final ScoreJpaRepository scoreJpaRepository;
    private final ScoreMapper scoreMapper;
    private final JPAQueryFactory jpaQueryFactory;

    private final CategoryPersistencePort categoryPersistencePort;

    @Override
    public Score findScoreByNameAndEmail(String name, String email) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(scoreJpaEntity)
                        .join(scoreJpaEntity.category)
                        .where(scoreJpaEntity.category.name.eq(name))
                        .where(scoreJpaEntity.member.email.eq(email))
                        .fetchOne()
        ).map(scoreMapper::toDomain).orElse(null);
    }

    @Override
    public void saveScore(Score score) {
        scoreJpaRepository.saveAndFlush(scoreMapper.toEntity(score));
    }
}