package team.incude.gsmc.v2.domain.certificate.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.certificate.application.port.CertificatePersistencePort;
import team.incude.gsmc.v2.domain.certificate.domain.Certificate;
import team.incude.gsmc.v2.domain.certificate.exception.CertificateNotFoundException;
import team.incude.gsmc.v2.domain.certificate.persistence.mapper.CertificateMapper;
import team.incude.gsmc.v2.domain.certificate.persistence.projection.CertificateProjection;
import team.incude.gsmc.v2.domain.certificate.persistence.repository.CertificateJpaRepository;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;
import java.util.Optional;

import static team.incude.gsmc.v2.domain.certificate.persistence.entity.QCertificateJpaEntity.certificateJpaEntity;
import static team.incude.gsmc.v2.domain.member.persistence.entity.QMemberJpaEntity.memberJpaEntity;

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class CertificatePersistenceAdapter implements CertificatePersistencePort {

    private final CertificateJpaRepository certificateJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final CertificateMapper certificateMapper;

    @Override
    public Certificate findCertificateByIdWithLock(Long id) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(certificateJpaEntity)
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .where(certificateJpaEntity.id.eq(id))
                        .fetchOne()
        ).map(certificateMapper::toDomain).orElseThrow(CertificateNotFoundException::new);
    }

    @Override
    public List<Certificate> findCertificateByMemberEmail(String email) {
        return jpaQueryFactory
                .select(Projections.constructor(
                        CertificateProjection.class,
                        certificateJpaEntity.id,
                        certificateJpaEntity.name,
                        certificateJpaEntity.acquisitionDate,
                        certificateJpaEntity.evidence.fileUri
                ))
                .from(certificateJpaEntity)
                .join(certificateJpaEntity.member, memberJpaEntity)
                .where(memberJpaEntity.email.eq(email))
                .fetch()
                .stream()
                .map(certificateMapper::fromProjection)
                .toList();
    }

    @Override
    public Certificate saveCertificate(Certificate certificate) {
        return certificateMapper.toDomain(certificateJpaRepository.save(certificateMapper.toEntity(certificate)));
    }

    @Override
    public void deleteCertificateById(Long id) {
        certificateJpaRepository.deleteById(id);
    }
}