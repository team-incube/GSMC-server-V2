package team.incude.gsmc.v2.domain.score.persistence.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_category")
@Getter
@NoArgsConstructor
public class CategoryJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "maximum_value", nullable = false)
    private Integer maximumValue;

    @Column(name = "weight", nullable = false)
    private Integer weight;

    @Column(name = "is_evidence_required", nullable = false)
    private Boolean isEvidenceRequired;

    @Builder
    public CategoryJpaEntity(Long id, String name, Integer maximumValue, Integer weight, Boolean isEvidenceRequired) {
        this.id = id;
        this.name = name;
        this.maximumValue = maximumValue;
        this.weight = weight;
        this.isEvidenceRequired = isEvidenceRequired;
    }
}