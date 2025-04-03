package team.incude.gsmc.v2.domain.evidence.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.score.persistence.entity.ScoreJpaEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_evidence")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EvidenceJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evidence_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "score_id", nullable = false)
    private ScoreJpaEntity score;

    @Column(name = "evidence_type", nullable = false)
    private EvidenceType evidenceType;

    @Column(name = "review_status", nullable = false)
    private ReviewStatus reviewStatus;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
