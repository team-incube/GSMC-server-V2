package team.incude.gsmc.v2.domain.evidence.application.usecase;

import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;

public interface UpdateReviewStatusUseCase {
    void execute(Long evidenceId, ReviewStatus reviewStatus);
}
