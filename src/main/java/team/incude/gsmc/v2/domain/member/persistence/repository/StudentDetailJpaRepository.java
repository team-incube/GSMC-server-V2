package team.incude.gsmc.v2.domain.member.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.incude.gsmc.v2.domain.member.persistence.entity.StudentDetailJpaEntity;

@Repository
public interface StudentDetailJpaRepository extends JpaRepository<StudentDetailJpaEntity, Long> {
}
