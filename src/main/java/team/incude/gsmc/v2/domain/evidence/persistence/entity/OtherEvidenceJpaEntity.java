package team.incude.gsmc.v2.domain.evidence.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_other_evidence")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OtherEvidenceJpaEntity {
    @Id
    @MapsId
    @OneToOne
    @JoinColumn(name = "evidence_id")
    private EvidenceJpaEntity id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "book_author", nullable = false)
    private String author;

    @Column(name = "page", nullable = false)
    private Integer page;

    @Column(name = "content", nullable = false)
    private String content;
}