package team.incube.gsmc.v2.domain.certificate.persistence.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.incube.gsmc.v2.domain.certificate.domain.Certificate;
import team.incube.gsmc.v2.domain.certificate.persistence.entity.CertificateJpaEntity;
import team.incube.gsmc.v2.domain.certificate.persistence.projection.CertificateProjection;
import team.incube.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incube.gsmc.v2.domain.evidence.persistence.mapper.OtherEvidenceMapper;
import team.incube.gsmc.v2.domain.member.persistence.mapper.MemberMapper;
import team.incube.gsmc.v2.global.mapper.GenericMapper;

/**
 * 자격증 도메인과 JPA 엔티티 간 매핑을 담당하는 Mapper 클래스입니다.
 * <p>{@link Certificate}와 {@link CertificateJpaEntity}, {@link CertificateProjection} 간의 변환 로직을 제공합니다.
 * <ul>
 *   <li>toEntity: 도메인 객체 → JPA 엔티티</li>
 *   <li>toDomain: JPA 엔티티 → 도메인 객체</li>
 *   <li>fromProjection: Projection → 도메인 객체</li>
 * </ul>
 * <p>{@link MemberMapper}, {@link OtherEvidenceMapper}를 통해 연관된 객체도 함께 변환합니다.
 * @author snowykte0426
 */
@Component
@RequiredArgsConstructor
public class CertificateMapper implements GenericMapper<CertificateJpaEntity, Certificate> {

    private final MemberMapper memberMapper;
    private final OtherEvidenceMapper otherEvidenceMapper;

    /**
     * 도메인 객체를 JPA 엔티티로 변환합니다.
     * @param certificate 변환할 자격증 도메인 객체
     * @return 변환된 JPA 엔티티
     */
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

    /**
     * JPA 엔티티를 도메인 객체로 변환합니다.
     * @param certificateJpaEntity 변환할 자격증 JPA 엔티티
     * @return 변환된 도메인 객체
     */
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

    /**
     * Projection 객체를 도메인 객체로 변환합니다.
     * <p>쿼리 최적화를 위해 조회된 최소 필드 데이터를 도메인 객체로 매핑할 때 사용됩니다.
     * @param projection 변환할 자격증 Projection 객체
     * @return 변환된 도메인 객체
     */
    public Certificate fromProjection(CertificateProjection projection) {
        return Certificate.builder()
                .id(projection.id())
                .name(projection.name())
                .acquisitionDate(projection.acquisitionDate())
                .evidence(OtherEvidence.builder().fileUri(projection.fileUri()).build())
                .build();
    }
}