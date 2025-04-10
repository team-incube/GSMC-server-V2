package team.incude.gsmc.v2.domain.score.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.score.application.port.CategoryPersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Category;
import team.incude.gsmc.v2.domain.score.exception.CategoryNotFoundException;
import team.incude.gsmc.v2.domain.score.persistence.mapper.CategoryMapper;
import team.incude.gsmc.v2.domain.score.persistence.repository.CategoryJpaRepository;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.Optional;

import static team.incude.gsmc.v2.domain.score.persistence.entity.QCategoryJpaEntity.categoryJpaEntity;

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements CategoryPersistencePort {

    private final CategoryJpaRepository categoryJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final CategoryMapper categoryMapper;

    // TODO: 캐싱 적용으로 성능 최적화 어떠신가요?
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
    public void saveCategory(Category category) {
        categoryJpaRepository.save(categoryMapper.toEntity(category));
    }
}