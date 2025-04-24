package team.incude.gsmc.v2.domain.score.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import team.incude.gsmc.v2.domain.score.application.port.CategoryPersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Category;
import team.incude.gsmc.v2.domain.score.exception.CategoryNotFoundException;
import team.incude.gsmc.v2.domain.score.persistence.mapper.CategoryMapper;
import team.incude.gsmc.v2.domain.score.persistence.repository.CategoryJpaRepository;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;
import java.util.Optional;

import static team.incude.gsmc.v2.domain.score.persistence.entity.QCategoryJpaEntity.categoryJpaEntity;

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements CategoryPersistencePort {

    private final CategoryJpaRepository categoryJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final CategoryMapper categoryMapper;

    @Cacheable(value = "category", key = "#name")
    @Override
    public Category findCategoryByName(String name) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(categoryJpaEntity)
                        .where(categoryJpaEntity.name.eq(name))
                        .fetchOne()
        ).map(categoryMapper::toDomain).orElseThrow(CategoryNotFoundException::new);
    }

    @Override
    public List<Category> findAllCategory() {
        return categoryJpaRepository.findAll().stream().map(categoryMapper::toDomain).toList();
    }
}