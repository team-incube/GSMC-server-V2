package team.incude.gsmc.v2.domain.evidence.persistence.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_activity_evidence")
@Getter
@NoArgsConstructor
public class ActivityEvidenceJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "evidence_id")
    private EvidenceJpaEntity evidence;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "image_uri", unique = true)
    private String imageUri;

    @Builder
    public ActivityEvidenceJpaEntity(EvidenceJpaEntity evidence, String title, String content, String imageUri) {
        this.evidence = evidence;
        this.id = evidence.getId();
        this.title = title;
        this.content = content;
        this.imageUri = imageUri;
    }
}