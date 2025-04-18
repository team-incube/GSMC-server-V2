package team.incude.gsmc.v2.domain.member.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.incude.gsmc.v2.domain.member.persistence.entity.MemberJpaEntity;

import java.util.Optional;

@Repository
public interface MemberJpaRepository extends JpaRepository<MemberJpaEntity, Long> {
    Optional<MemberJpaEntity> findByEmail(String email);
}