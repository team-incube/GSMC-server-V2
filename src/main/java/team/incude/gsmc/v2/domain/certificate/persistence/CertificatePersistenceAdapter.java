package team.incude.gsmc.v2.domain.certificate.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.certificate.application.port.CertificatePersistencePort;
import team.incude.gsmc.v2.domain.certificate.domain.Certificate;
import team.incude.gsmc.v2.domain.certificate.persistence.mapper.CertificateMapper;
import team.incude.gsmc.v2.domain.certificate.persistence.repository.CertificateJpaRepository;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;

import static team.incude.gsmc.v2.domain.certificate.persistence.entity.QCertificateJpaEntity.certificateJpaEntity;

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class CertificatePersistenceAdapter implements CertificatePersistencePort {

    private final CertificateJpaRepository certificateJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final CertificateMapper certificateMapper;

    @Override
    public List<Certificate> findCertificateByEmail(String email) {
        return jpaQueryFactory
                .selectFrom(certificateJpaEntity)
                .where(certificateJpaEntity.member.email.eq(email))
                .fetch()
                .stream()
                .map(certificateMapper::toDomain)
                .toList();
    }
}