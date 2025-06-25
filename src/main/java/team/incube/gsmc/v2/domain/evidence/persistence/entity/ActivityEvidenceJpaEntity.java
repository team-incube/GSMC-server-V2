package team.incube.gsmc.v2.domain.evidence.persistence.entity;

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
    @Column(name = "evidence_id", unique = true)
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "evidence_id", unique = true)
    @MapsId
    private EvidenceJpaEntity evidence;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "image_uri", unique = true)
    private String imageUri;

    @Builder
    public ActivityEvidenceJpaEntity(Long id, EvidenceJpaEntity evidence, String title, String content, String imageUri) {
        this.id = id;
        this.evidence = evidence;
        this.title = title;
        this.content = content;
        this.imageUri = imageUri;
    }
}