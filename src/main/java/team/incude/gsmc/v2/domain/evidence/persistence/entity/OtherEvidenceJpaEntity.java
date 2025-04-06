package team.incude.gsmc.v2.domain.evidence.persistence.entity;

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
    @MapsId
    @OneToOne
    @JoinColumn(name = "evidence_id")
    private EvidenceJpaEntity id;

    @Column(name = "file_uri", nullable = false, unique = true)
    private String fileUri;

    @Builder
    public OtherEvidenceJpaEntity(EvidenceJpaEntity id, String fileUri) {
        this.id = id;
        this.fileUri = fileUri;
    }
}