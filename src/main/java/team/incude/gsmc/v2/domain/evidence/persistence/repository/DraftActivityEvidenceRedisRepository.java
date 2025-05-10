package team.incude.gsmc.v2.domain.evidence.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import team.incude.gsmc.v2.domain.evidence.persistence.entity.DraftActivityEvidenceRedisEntity;

import java.util.List;
import java.util.UUID;

public interface DraftActivityEvidenceRedisRepository extends CrudRepository<DraftActivityEvidenceRedisEntity, UUID> {
    List<DraftActivityEvidenceRedisEntity> findByEmail(String email);
}
