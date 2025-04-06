package team.incude.gsmc.v2.domain.score.persistence.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.score.domain.Category;
import team.incude.gsmc.v2.domain.score.persistence.entity.CategoryJpaEntity;
import team.incude.gsmc.v2.global.mapper.GenericMapper;

@Component
@RequiredArgsConstructor
public class CategoryMapper implements GenericMapper<CategoryJpaEntity, Category> {

    @Override
    public CategoryJpaEntity toEntity(Category category) {
        return CategoryJpaEntity.builder()
                .id(category.getId())
                .name(category.getName())
                .weight(category.getWeight())
                .maximumValue(category.getMaximumValue())
                .isEvidenceRequired(category.getIsEvidenceRequired())
                .isLimitedBySemester(category.getIsLimitedBySemester())
                .build();
    }

    @Override
    public Category toDomain(CategoryJpaEntity categoryJpaEntity) {
        return Category.builder()
                .id(categoryJpaEntity.getId())
                .name(categoryJpaEntity.getName())
                .weight(categoryJpaEntity.getWeight())
                .maximumValue(categoryJpaEntity.getMaximumValue())
                .isEvidenceRequired(categoryJpaEntity.getIsEvidenceRequired())
                .isLimitedBySemester(categoryJpaEntity.getIsLimitedBySemester())
                .build();
    }
}