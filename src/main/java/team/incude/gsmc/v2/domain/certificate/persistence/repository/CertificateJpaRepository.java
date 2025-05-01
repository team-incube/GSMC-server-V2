package team.incude.gsmc.v2.domain.certificate.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.incude.gsmc.v2.domain.certificate.persistence.entity.CertificateJpaEntity;

/**
 * 자격증 엔티티를 위한 JPA Repository 인터페이스입니다.
 * <p>기본적인 CRUD 연산을 상속받으며, 자격증 엔티티와 데이터베이스 간의 직접적인 연동을 담당합니다.
 * <p>추가적인 쿼리 메서드는
 * 별도의 QueryDSL 기반 구현체에서 처리할 수 있습니다.
 * @author snowykte0426
 */
public interface CertificateJpaRepository extends JpaRepository<CertificateJpaEntity, Long> {
}