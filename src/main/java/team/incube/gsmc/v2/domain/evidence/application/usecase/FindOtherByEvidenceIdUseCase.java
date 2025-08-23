package team.incube.gsmc.v2.domain.evidence.application.usecase;

import team.incube.gsmc.v2.domain.evidence.presentation.data.response.GetOtherEvidenceResponse;

public interface FindOtherByEvidenceIdUseCase {
    GetOtherEvidenceResponse execute(Long Id);
}
