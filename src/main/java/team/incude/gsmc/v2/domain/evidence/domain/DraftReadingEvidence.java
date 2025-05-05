package team.incude.gsmc.v2.domain.evidence.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class DraftReadingEvidence {
    private UUID id;
    private String title;
    private String author;
    private Integer page;
    private String content;
    private Long ttl;
}
