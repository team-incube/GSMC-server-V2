package team.incube.gsmc.v2.domain.auth.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import team.incube.gsmc.v2.domain.auth.persistence.entity.AuthenticationRedisEntity;

import java.util.Optional;

/**
 * 이메일 인증 상태를 Redis에 저장하고 조회하기 위한 Spring Data Repository입니다.
 * <p>{@link AuthenticationRedisEntity}를 기반으로 Redis에 저장된 인증 객체를 관리하며,
 * 이메일 인증 여부를 TTL 기반으로 추적할 수 있도록 지원합니다.
 * 주요 메서드:
 * <ul>
 *   <li>{@code findByEmail} - 이메일을 기준으로 인증 상태 조회</li>
 * </ul>
 * 이 Repository는 Redis에 사용자 인증 여부를 저장하여 회원가입 및 비밀번호 변경 과정에서 활용됩니다.
 * @author jihoonwjj
 */
@Repository
public interface AuthenticationRedisRepository extends CrudRepository<AuthenticationRedisEntity, String> {

    Optional<AuthenticationRedisEntity> findByEmail(String email);
}
