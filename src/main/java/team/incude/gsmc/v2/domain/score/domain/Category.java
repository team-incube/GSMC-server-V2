package team.incude.gsmc.v2.domain.score.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Category {
    private Long id;
    private String name;
    private Integer maximumValue;
    private Integer weight;
    private Boolean isEvidenceRequired;
    private Boolean isLimitedBySemester;

    public Category(String name, Integer maximumValue, Integer weight, Boolean isEvidenceRequired, Boolean isLimitedBySemester) {
        this.name = name;
        this.maximumValue = maximumValue;
        this.weight = weight;
        this.isEvidenceRequired = isEvidenceRequired;
        this.isLimitedBySemester = isLimitedBySemester;
    }
}