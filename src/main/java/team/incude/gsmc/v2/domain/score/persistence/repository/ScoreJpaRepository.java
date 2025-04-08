package team.incude.gsmc.v2.domain.score.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.incude.gsmc.v2.domain.score.persistence.entity.ScoreJpaEntity;

@Repository
public interface ScoreJpaRepository extends JpaRepository<ScoreJpaEntity, Long> {
}