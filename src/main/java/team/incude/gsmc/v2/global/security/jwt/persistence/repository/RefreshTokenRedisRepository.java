package team.incude.gsmc.v2.global.security.jwt.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import team.incude.gsmc.v2.global.security.jwt.persistence.entity.RefreshTokenRedisEntity;

@Repository
public interface RefreshTokenRedisRepository extends CrudRepository<RefreshTokenRedisEntity, String> {
}
