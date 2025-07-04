package team.incube.gsmc.v2.domain.evidence.persistence.entity;

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
    @Column(name = "evidence_id", unique = true)
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "evidence_id")
    @MapsId
    private EvidenceJpaEntity evidence;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "book_author", nullable = false)
    private String author;

    @Column(name = "page", nullable = false)
    private Integer page;

    @Column(name = "content", nullable = false)
    private String content;

    @Builder
    public ReadingEvidenceJpaEntity(Long id, EvidenceJpaEntity evidence, String title, String author, Integer page, String content) {
        this.id = id;
        this.evidence = evidence;
        this.title = title;
        this.author = author;
        this.page = page;
        this.content = content;
    }
}