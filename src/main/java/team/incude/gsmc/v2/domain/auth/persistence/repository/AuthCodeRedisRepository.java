package team.incude.gsmc.v2.domain.auth.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import team.incude.gsmc.v2.domain.auth.persistence.entity.AuthCodeRedisEntity;

/**
 * 인증 코드 정보를 Redis에 저장하고 관리하기 위한 Spring Data Repository입니다.
 * <p>{@link AuthCodeRedisEntity}를 기반으로 Redis에서 인증 코드의 생성, 조회, 삭제를 처리합니다.
 * <p>주로 이메일 인증 시 사용자의 인증 코드를 TTL 기반으로 관리하기 위해 사용됩니다.
 * 주요 메서드:
 * <ul>
 *   <li>{@code findByAuthCode} - 인증 코드로 인증 객체 조회</li>
 *   <li>{@code existsByAuthCode} - 인증 코드 존재 여부 확인</li>
 *   <li>{@code deleteByAuthCode} - 인증 코드 삭제</li>
 * </ul>
 * 이 Repository는 Spring Data Redis를 기반으로 하며, 키는 이메일(String), 값은 인증 엔티티입니다.
 * @author jihoonwjj
 */
@Repository
public interface AuthCodeRedisRepository extends CrudRepository<AuthCodeRedisEntity, String> {

    AuthCodeRedisEntity findByAuthCode(String code);

    Boolean existsByAuthCode(String code);

    void deleteByAuthCode(String code);
}
