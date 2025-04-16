package team.incude.gsmc.v2.domain.score.persistence.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.member.persistence.mapper.MemberMapper;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.domain.score.persistence.entity.ScoreJpaEntity;
import team.incude.gsmc.v2.global.mapper.GenericMapper;

@Component
@RequiredArgsConstructor
public class ScoreMapper implements GenericMapper<ScoreJpaEntity, Score> {

    private final MemberMapper memberMapper;
    private final CategoryMapper categoryMapper;

    @Override
    public ScoreJpaEntity toEntity(Score score) {
        return ScoreJpaEntity.builder()
                .id(score.getId())
                .member(memberMapper.toEntity(score.getMember()))
                .category(categoryMapper.toEntity(score.getCategory()))
                .value(score.getValue())
                .build();
    }

    @Override
    public Score toDomain(ScoreJpaEntity scoreJpaEntity) {
        return Score.builder()
                .id(scoreJpaEntity.getId())
                .member(memberMapper.toDomain(scoreJpaEntity.getMember()))
                .category(categoryMapper.toDomain(scoreJpaEntity.getCategory()))
                .value(scoreJpaEntity.getValue())
                .build();
    }
}