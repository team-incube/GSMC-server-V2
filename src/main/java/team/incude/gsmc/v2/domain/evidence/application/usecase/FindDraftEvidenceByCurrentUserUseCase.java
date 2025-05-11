package team.incude.gsmc.v2.domain.evidence.application.usecase;

import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetDraftEvidenceResponse;

public interface FindDraftEvidenceByCurrentUserUseCase {
    GetDraftEvidenceResponse execute();
}
