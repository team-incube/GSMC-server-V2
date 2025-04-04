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
    @MapsId
    @OneToOne
    @JoinColumn(name = "evidence_id")
    private EvidenceJpaEntity id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "image_url")
    private String imageUrl;

    @Builder
    public ActivityEvidenceJpaEntity(EvidenceJpaEntity id, String title, String content, String imageUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }
}