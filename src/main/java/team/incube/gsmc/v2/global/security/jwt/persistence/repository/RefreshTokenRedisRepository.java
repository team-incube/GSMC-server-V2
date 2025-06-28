package team.incube.gsmc.v2.global.security.jwt.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import team.incube.gsmc.v2.global.security.jwt.persistence.entity.RefreshTokenRedisEntity;

/**
 * Redis에 저장되는 Refresh Token 엔티티를 관리하는 리포지토리 인터페이스입니다.
 * <p>이 인터페이스는 Spring Data Redis의 CrudRepository를 확장하여, Refresh Token 엔티티에 대한 CRUD
 * (생성, 읽기, 업데이트, 삭제) 작업을 지원합니다. Redis에 저장된 Refresh Token을 효율적으로 관리할 수 있도록
 * 설계되었습니다.
 * @author jihoonwjj
 */
@Repository
public interface RefreshTokenRedisRepository extends CrudRepository<RefreshTokenRedisEntity, String> {
}