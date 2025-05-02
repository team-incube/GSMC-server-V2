package team.incude.gsmc.v2.domain.score.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.incude.gsmc.v2.domain.score.persistence.entity.ScoreJpaEntity;

/**
 * 점수(Score) 도메인의 JPA Repository 인터페이스입니다.
 * <p>{@link ScoreJpaEntity}에 대한 기본적인 CRUD 연산을 제공합니다.
 * Spring Data JPA에 의해 자동으로 구현되며, 점수 정보를 저장하거나 조회할 때 사용됩니다.
 * <p>복잡한 쿼리나 커스텀 조회가 필요한 경우, 별도의 QueryDSL 기반 구현체에서 처리할 수 있습니다.
 * @author snowykte0426
 */
@Repository
public interface ScoreJpaRepository extends JpaRepository<ScoreJpaEntity, Long> {
}