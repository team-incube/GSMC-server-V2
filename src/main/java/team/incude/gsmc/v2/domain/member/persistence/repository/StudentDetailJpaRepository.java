package team.incude.gsmc.v2.domain.member.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.incude.gsmc.v2.domain.member.persistence.entity.StudentDetailJpaEntity;

/**
 * 학생 상세 정보(StudentDetail) JPA Repository 인터페이스입니다.
 * <p>{@link StudentDetailJpaEntity}에 대한 기본적인 CRUD 연산을 제공합니다.
 * Spring Data JPA를 기반으로 하며, 도메인 영속성 계층과 DB 간의 상호작용을 추상화합니다.
 * <p>복잡한 조건의 쿼리나 커스텀 구현이 필요한 경우 QueryDSL 또는 별도 Repository 커스텀 구현체를 통해 확장할 수 있습니다.
 * @author jihoonwjj
 */
@Repository
public interface StudentDetailJpaRepository extends JpaRepository<StudentDetailJpaEntity, Long> {
}