package team.incube.gsmc.v2.domain.score.persistence.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.incube.gsmc.v2.domain.member.persistence.mapper.MemberMapper;
import team.incube.gsmc.v2.domain.score.domain.Score;
import team.incube.gsmc.v2.domain.score.persistence.entity.ScoreJpaEntity;
import team.incube.gsmc.v2.global.mapper.GenericMapper;

/**
 * 점수 도메인과 JPA 엔티티 간의 매핑을 수행하는 Mapper 클래스입니다.
 * <p>{@link Score}와 {@link ScoreJpaEntity} 간의 변환을 담당하며,
 * 내부적으로 {@link MemberMapper}와 {@link CategoryMapper}를 활용하여 관련 객체도 함께 매핑합니다.
 * <p>이 매퍼는 도메인 계층과 영속성 계층 간의 의존성을 분리하고 일관된 변환 로직을 제공합니다.
 * @author snowykte0426
 */
@Component
@RequiredArgsConstructor
public class ScoreMapper implements GenericMapper<ScoreJpaEntity, Score> {

    private final MemberMapper memberMapper;
    private final CategoryMapper categoryMapper;

    /**
     * 도메인 객체를 JPA 엔티티로 변환합니다.
     * @param score 도메인 점수 객체
     * @return 변환된 JPA 엔티티 객체
     */
    @Override
    public ScoreJpaEntity toEntity(Score score) {
        return ScoreJpaEntity.builder()
                .id(score.getId())
                .member(memberMapper.toEntity(score.getMember()))
                .category(categoryMapper.toEntity(score.getCategory()))
                .value(score.getValue())
                .build();
    }

    /**
     * JPA 엔티티를 도메인 객체로 변환합니다.
     * @param scoreJpaEntity 점수 JPA 엔티티 객체
     * @return 변환된 도메인 점수 객체
     */
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