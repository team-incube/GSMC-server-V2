package team.incude.gsmc.v2.domain.score.persistence.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.score.domain.Category;
import team.incude.gsmc.v2.domain.score.persistence.entity.CategoryJpaEntity;
import team.incude.gsmc.v2.global.mapper.GenericMapper;

/**
 * 점수 카테고리 도메인과 JPA 엔티티 간의 매핑을 담당하는 클래스입니다.
 * <p>{@link Category}와 {@link CategoryJpaEntity} 간의 변환을 구현하며,
 * 영속성 계층과 도메인 계층 간의 의존성을 분리하는 역할을 합니다.
 * <p>{@link GenericMapper}를 구현하여 일관된 변환 인터페이스를 제공합니다.
 * @author snowykte0426
 */
@Component
@RequiredArgsConstructor
public class CategoryMapper implements GenericMapper<CategoryJpaEntity, Category> {

    /**
     * 도메인 객체를 JPA 엔티티로 변환합니다.
     * @param category 변환할 도메인 객체
     * @return 변환된 JPA 엔티티
     */
    @Override
    public CategoryJpaEntity toEntity(Category category) {
        return CategoryJpaEntity.builder()
                .id(category.getId())
                .name(category.getName())
                .weight(category.getWeight())
                .maximumValue(category.getMaximumValue())
                .isEvidenceRequired(category.getIsEvidenceRequired())
                .koreanName(category.getKoreanName())
                .build();
    }

    /**
     * JPA 엔티티를 도메인 객체로 변환합니다.
     * @param categoryJpaEntity 변환할 JPA 엔티티
     * @return 변환된 도메인 객체
     */
    @Override
    public Category toDomain(CategoryJpaEntity categoryJpaEntity) {
        return Category.builder()
                .id(categoryJpaEntity.getId())
                .name(categoryJpaEntity.getName())
                .weight(categoryJpaEntity.getWeight())
                .maximumValue(categoryJpaEntity.getMaximumValue())
                .isEvidenceRequired(categoryJpaEntity.getIsEvidenceRequired())
                .koreanName(categoryJpaEntity.getKoreanName())
                .build();
    }
}