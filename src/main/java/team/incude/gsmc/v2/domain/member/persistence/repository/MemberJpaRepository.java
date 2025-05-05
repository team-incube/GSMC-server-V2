package team.incude.gsmc.v2.domain.member.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.incude.gsmc.v2.domain.member.persistence.entity.MemberJpaEntity;

import java.util.Optional;

/**
 * 사용자(Member) 엔티티에 대한 Spring Data JPA Repository입니다.
 * <p>사용자의 기본 CRUD 연산을 제공하며,
 * JPA의 기본 기능을 상속하여 DB와의 상호작용을 추상화합니다.
 * @author snowykte0426, jihoonwjj
 */
@Repository
public interface MemberJpaRepository extends JpaRepository<MemberJpaEntity, Long> {
}