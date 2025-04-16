package team.incude.gsmc.v2.domain.auth.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import team.incude.gsmc.v2.domain.auth.persistence.entity.AuthenticationRedisEntity;

import java.util.Optional;

@Repository
public interface AuthenticationRedisRepository extends CrudRepository<AuthenticationRedisEntity, String> {

    Optional<AuthenticationRedisEntity> findByEmail(String email);
}
