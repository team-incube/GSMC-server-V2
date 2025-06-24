package team.incube.gsmc.v2.domain.score.persistence.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.incube.gsmc.v2.domain.member.persistence.entity.MemberJpaEntity;

@Entity
@Table(name = "tb_score")
@Getter
@NoArgsConstructor
public class ScoreJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberJpaEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryJpaEntity category;

    @Column(name = "value", nullable = false)
    private Integer value;

    @Builder
    public ScoreJpaEntity(Long id, MemberJpaEntity member, CategoryJpaEntity category, Integer value) {
        this.id = id;
        this.member = member;
        this.category = category;
        this.value = value;
    }
}