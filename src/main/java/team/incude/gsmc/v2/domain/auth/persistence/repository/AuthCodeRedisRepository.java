package team.incude.gsmc.v2.domain.auth.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import team.incude.gsmc.v2.domain.auth.persistence.entity.AuthCodeRedisEntity;

@Repository
public interface AuthCodeRedisRepository extends CrudRepository<AuthCodeRedisEntity, String> {

    AuthCodeRedisEntity findByAuthCode(String code);

    Boolean existsByAuthCode(String code);

    void deleteByAuthCode(String code);
}
