package team.incube.gsmc.v2.domain.evidence.persistence.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.incube.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incube.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incube.gsmc.v2.domain.score.persistence.entity.ScoreJpaEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_evidence")
@Getter
@NoArgsConstructor
public class EvidenceJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evidence_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "score_id", nullable = false)
    private ScoreJpaEntity score;

    @Column(name = "evidence_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EvidenceType evidenceType;

    @Column(name = "review_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewStatus;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public EvidenceJpaEntity(Long id, ScoreJpaEntity score, EvidenceType evidenceType, ReviewStatus reviewStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.score = score;
        this.evidenceType = evidenceType;
        this.reviewStatus = reviewStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}