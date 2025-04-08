package team.incude.gsmc.v2.domain.evidence.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.incude.gsmc.v2.domain.evidence.persistence.entity.ActivityEvidenceJpaEntity;

public interface ActivityEvidenceJpaRepository extends JpaRepository<ActivityEvidenceJpaEntity, Long> {
}
