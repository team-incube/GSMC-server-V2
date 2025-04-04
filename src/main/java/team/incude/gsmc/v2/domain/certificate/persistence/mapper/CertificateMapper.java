package team.incude.gsmc.v2.domain.certificate.persistence.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.certificate.domain.Certificate;
import team.incude.gsmc.v2.domain.certificate.persistence.entity.CertificateJpaEntity;
import team.incude.gsmc.v2.domain.certificate.persistence.projection.CertificateProjection;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.evidence.persistence.mapper.OtherEvidenceMapper;
import team.incude.gsmc.v2.domain.member.persistence.mapper.MemberMapper;
import team.incude.gsmc.v2.global.mapper.GenericMapper;

@Component
@RequiredArgsConstructor
public class CertificateMapper implements GenericMapper<CertificateJpaEntity, Certificate> {

    private final MemberMapper memberMapper;
    private final OtherEvidenceMapper otherEvidenceMapper;

    @Override
    public CertificateJpaEntity toEntity(Certificate certificate) {
        return CertificateJpaEntity.builder()
                .id(certificate.getId())
                .member(memberMapper.toEntity(certificate.getMember()))
                .evidence(otherEvidenceMapper.toEntity(certificate.getEvidence()))
                .name(certificate.getName())
                .acquisitionDate(certificate.getAcquisitionDate())
                .build();
    }

    @Override
    public Certificate toDomain(CertificateJpaEntity certificateJpaEntity) {
        return Certificate.builder()
                .id(certificateJpaEntity.getId())
                .member(memberMapper.toDomain(certificateJpaEntity.getMember()))
                .evidence(otherEvidenceMapper.toDomain(certificateJpaEntity.getEvidence()))
                .name(certificateJpaEntity.getName())
                .acquisitionDate(certificateJpaEntity.getAcquisitionDate())
                .build();
    }

    public Certificate fromProjection(CertificateProjection projection) {
        return Certificate.builder()
                .id(projection.id())
                .name(projection.name())
                .acquisitionDate(projection.acquisitionDate())
                .evidence(OtherEvidence.builder().fileUri(projection.fileUri()).build())
                .build();
    }
}