package team.incube.gsmc.v2.domain.evidence.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReadingEvidence {
    private Evidence id;
    private String title;
    private String author;
    private Integer page;
    private String content;
}