package team.incube.gsmc.v2.domain.certificate.persistence.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.incube.gsmc.v2.domain.evidence.persistence.entity.OtherEvidenceJpaEntity;
import team.incube.gsmc.v2.domain.member.persistence.entity.MemberJpaEntity;

import java.time.LocalDate;

@Entity
@Table(name = "tb_certificate")
@Getter
@NoArgsConstructor
public class CertificateJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certificate_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberJpaEntity member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evidence_id", nullable = false)
    private OtherEvidenceJpaEntity evidence;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "acquisition_date", nullable = false)
    private LocalDate acquisitionDate;

    @Builder
    public CertificateJpaEntity(Long id, MemberJpaEntity member, OtherEvidenceJpaEntity evidence, String name, LocalDate acquisitionDate) {
        this.id = id;
        this.member = member;
        this.evidence = evidence;
        this.name = name;
        this.acquisitionDate = acquisitionDate;
    }
}