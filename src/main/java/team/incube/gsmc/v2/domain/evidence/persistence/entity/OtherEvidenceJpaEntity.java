package team.incube.gsmc.v2.domain.evidence.persistence.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_other_evidence")
@Getter
@NoArgsConstructor
public class OtherEvidenceJpaEntity {
    @Id
    @Column(name = "evidence_id", unique = true)
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "evidence_id", unique = true)
    @MapsId
    private EvidenceJpaEntity evidence;

    @Column(name = "file_uri", nullable = false, unique = true)
    private String fileUri;

    @Builder
    public OtherEvidenceJpaEntity(Long id, EvidenceJpaEntity evidence, String fileUri) {
        this.id = id;
        this.evidence = evidence;
        this.fileUri = fileUri;
    }
}