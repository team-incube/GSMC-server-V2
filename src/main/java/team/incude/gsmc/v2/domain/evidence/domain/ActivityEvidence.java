package team.incude.gsmc.v2.domain.evidence.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ActivityEvidence {
    private Evidence id;
    private String title;
    private String content;
    private String imageUrl;
}