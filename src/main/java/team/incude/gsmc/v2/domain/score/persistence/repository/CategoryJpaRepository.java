package team.incude.gsmc.v2.domain.score.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.incude.gsmc.v2.domain.score.persistence.entity.CategoryJpaEntity;

/**
 * 점수 카테고리(Category) JPA Repository입니다.
 * <p>{@link CategoryJpaEntity}에 대한 기본적인 CRUD 기능을 제공합니다.
 * Spring Data JPA에 의해 구현되며, 영속성 계층에서 카테고리 정보를 조회하거나 저장할 때 사용됩니다.
 * <p>추가 쿼리가 필요한 경우 QueryDSL을 사용하는 커스텀 구현체를 작성할 수 있습니다.
 * @author snowykte0426
 */
@Repository
public interface CategoryJpaRepository extends JpaRepository<CategoryJpaEntity, Long> {
}