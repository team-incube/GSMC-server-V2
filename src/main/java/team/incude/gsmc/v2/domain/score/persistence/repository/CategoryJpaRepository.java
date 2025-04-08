package team.incude.gsmc.v2.domain.score.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.incude.gsmc.v2.domain.score.persistence.entity.CategoryJpaEntity;

@Repository
public interface CategoryJpaRepository extends JpaRepository<CategoryJpaEntity, Long> {
}