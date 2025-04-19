package team.incude.gsmc.v2.domain.evidence.application.usecase;

import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetEvidencesResponse;

public interface FindEvidenceByEmailAndFilteringTypeAndStatusUseCase {
    GetEvidencesResponse execute(String email, EvidenceType type, ReviewStatus status);
}
