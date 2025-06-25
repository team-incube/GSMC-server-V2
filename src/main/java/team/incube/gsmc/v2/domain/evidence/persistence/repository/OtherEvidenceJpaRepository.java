package team.incube.gsmc.v2.domain.evidence.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.incube.gsmc.v2.domain.evidence.persistence.entity.OtherEvidenceJpaEntity;

/**
 * 기타 증빙자료에 대한 JPA Repository 인터페이스입니다.
 * <p>{@link OtherEvidenceJpaEntity}를 대상으로 기본적인 CRUD 작업을 수행할 수 있도록 지원합니다.
 * Spring Data JPA에 의해 구현체가 자동 생성됩니다.
 * 주요 기능:
 * <ul>
 *     <li>ID 기반 조회, 저장, 삭제</li>
 *     <li>향후 Query Method 또는 @Query 기반 확장 가능</li>
 * </ul>
 * @author suuuuuuminnnnnn
 */
public interface OtherEvidenceJpaRepository extends JpaRepository<OtherEvidenceJpaEntity, Long> {
}