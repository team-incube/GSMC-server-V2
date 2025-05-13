package team.incude.gsmc.v2.domain.evidence.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import team.incude.gsmc.v2.domain.evidence.persistence.entity.DraftReadingEvidenceRedisEntity;

import java.util.List;
import java.util.UUID;

public interface DraftReadingEvidenceRedisRepository extends CrudRepository<DraftReadingEvidenceRedisEntity, UUID> {
    List<DraftReadingEvidenceRedisEntity> findByEmail(String email);
}
