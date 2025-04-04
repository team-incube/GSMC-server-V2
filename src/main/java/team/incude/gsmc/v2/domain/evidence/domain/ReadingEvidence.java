package team.incude.gsmc.v2.domain.evidence.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import team.incude.gsmc.v2.domain.evidence.persistence.entity.EvidenceJpaEntity;

@Getter
@Builder
public class ReadingEvidence {
    private Evidence id;
    private String title;
    private String author;
    private Integer page;
    private String content;
}