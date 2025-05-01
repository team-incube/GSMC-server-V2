package team.incude.gsmc.v2.domain.evidence.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import team.incude.gsmc.v2.domain.evidence.persistence.entity.ReadingEvidenceRedisEntity;

import java.util.UUID;

public interface DraftReadingEvidenceRedisRepository extends CrudRepository<ReadingEvidenceRedisEntity, UUID> {
}
