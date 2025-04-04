package team.incude.gsmc.v2.domain.certificate.persistence.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.certificate.domain.Certificate;
import team.incude.gsmc.v2.domain.certificate.persistence.entity.CertificateJpaEntity;
import team.incude.gsmc.v2.domain.member.persistence.mapper.MemberMapper;
import team.incude.gsmc.v2.global.mapper.GenericMapper;

@Component
@RequiredArgsConstructor
public class CertificateMapper implements GenericMapper<CertificateJpaEntity, Certificate> {

    private final MemberMapper memberMapper;

    @Override
    public CertificateJpaEntity toEntity(Certificate certificate) {
        return CertificateJpaEntity.builder()
                .id(certificate.getId())
                .member(memberMapper.toEntity(certificate.getMember()))
                .name(certificate.getName())
                .acquisitionDate(certificate.getAcquisitionDate())
                .build();
    }

    @Override
    public Certificate toDomain(CertificateJpaEntity certificateJpaEntity) {
        return Certificate.builder()
                .id(certificateJpaEntity.getId())
                .member(memberMapper.toDomain(certificateJpaEntity.getMember()))
                .name(certificateJpaEntity.getName())
                .acquisitionDate(certificateJpaEntity.getAcquisitionDate())
                .build();
    }
}