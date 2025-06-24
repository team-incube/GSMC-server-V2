package team.incube.gsmc.v2.domain.evidence.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.incube.gsmc.v2.domain.evidence.persistence.entity.ActivityEvidenceJpaEntity;

/**
 * 활동 증빙자료에 대한 JPA Repository 인터페이스입니다.
 * <p>{@link ActivityEvidenceJpaEntity}를 대상으로 기본적인 CRUD 작업을 수행할 수 있도록 지원합니다.
 * <p>Spring Data JPA에 의해 구현체가 자동 생성됩니다.
 * 주요 기능:
 * <ul>
 *     <li>findById, save, delete 등 기본 메서드 제공</li>
 *     <li>추후 Query Method 또는 @Query 기반 확장 가능</li>
 * </ul>
 * @author suuuuuuminnnnnn
 */
public interface ActivityEvidenceJpaRepository extends JpaRepository<ActivityEvidenceJpaEntity, Long> {
}
