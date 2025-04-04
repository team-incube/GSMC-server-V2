package team.incude.gsmc.v2.domain.evidence.persistence.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_reading_evidence")
@Getter
@NoArgsConstructor
public class ReadingEvidenceJpaEntity {
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

    @Builder
    public ReadingEvidenceJpaEntity(EvidenceJpaEntity id, String title, String author, Integer page, String content) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.page = page;
        this.content = content;
    }
}