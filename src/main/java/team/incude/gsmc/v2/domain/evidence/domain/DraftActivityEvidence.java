package team.incude.gsmc.v2.domain.evidence.domain;

import lombok.Builder;
import lombok.Getter;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;

import java.util.UUID;

@Getter
@Builder
public class DraftActivityEvidence {
    private UUID id;
    private String categoryName;
    private String title;
    private String content;
    private String imageUrl;
    private EvidenceType evidenceType;
    private Long ttl;
    private String email;
}
