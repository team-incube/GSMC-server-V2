package team.incube.gsmc.v2.domain.evidence.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import team.incube.gsmc.v2.domain.evidence.persistence.entity.DraftActivityEvidenceRedisEntity;

import java.util.List;
import java.util.UUID;

/**
 * 활동 증빙자료 임시저장을 위한 Redis 기반 JPA Repository 인터페이스입니다.
 * <p>{@link DraftActivityEvidenceRedisEntity}를 Redis에 저장/조회/삭제하는 데 사용되며,
 * Spring Data Redis를 기반으로 동작합니다.
 * 주요 기능:
 * <ul>
 *     <li>UUID 기반 기본 CRUD 제공</li>
 *     <li>사용자 이메일로 임시저장 목록 조회</li>
 * </ul>
 * @author suuuuuuminnnnnn
 */
public interface DraftActivityEvidenceRedisRepository extends CrudRepository<DraftActivityEvidenceRedisEntity, UUID> {
    List<DraftActivityEvidenceRedisEntity> findByEmail(String email);
}
