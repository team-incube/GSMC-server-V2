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

/**
 * 점수 분류(Category) 도메인의 영속성 어댑터 구현체입니다.
 * <p>{@link CategoryPersistencePort}를 구현하며, JPA 및 QueryDSL을 활용해 카테고리 정보를 조회합니다.
 * 도메인 객체와 엔티티 간의 변환은 {@link CategoryMapper}를 통해 수행됩니다.
 * <p>제공 기능:
 * <ul>
 *   <li>{@code findCategoryByName(String name)} - 이름을 기준으로 카테고리 조회 (캐싱 적용)</li>
 *   <li>{@code findAllCategory()} - 전체 카테고리 목록 조회</li>
 * </ul>
 * @author snowykte0426
 */
@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements CategoryPersistencePort {

    private final CategoryJpaRepository categoryJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final CategoryMapper categoryMapper;

    /**
     * 모든 카테고리 목록을 조회합니다.
     * @return 도메인 카테고리 객체 리스트
     */
    @Cacheable(value = "categories", key = "'ALL'")
    @Override
    public List<Category> findAllCategory() {
        return categoryJpaRepository.findAll().stream().map(categoryMapper::toDomain).toList();
    }
}