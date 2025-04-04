package team.incude.gsmc.v2.domain.evidence.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OtherEvidence {
    private Evidence id;
    private String fileUri;
}