package team.incude.gsmc.v2.domain.score.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.incude.gsmc.v2.domain.member.persistence.entity.MemberJpaEntity;

@Entity
@Table(name = "tb_score")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScoreJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberJpaEntity member;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryJpaEntity category;

    @Column(name = "value", nullable = false)
    private Integer value;

    @Column(name = "semester")
    private Integer semester;
}