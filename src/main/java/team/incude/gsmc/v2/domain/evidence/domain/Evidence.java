package team.incude.gsmc.v2.domain.evidence.domain;

import lombok.Builder;
import lombok.Getter;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.score.domain.Score;

import java.time.LocalDateTime;

@Getter
@Builder
public class Evidence {
    private Long id;
    private Score score;
    private EvidenceType evidenceType;
    private ReviewStatus reviewStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}