package team.incude.gsmc.v2.domain.certificate.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.incude.gsmc.v2.domain.certificate.persistence.entity.CertificateJpaEntity;

public interface CertificateJpaRepository extends JpaRepository<CertificateJpaEntity, Long> {
}