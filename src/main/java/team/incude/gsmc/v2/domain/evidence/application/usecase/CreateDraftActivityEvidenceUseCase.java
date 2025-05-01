package team.incude.gsmc.v2.domain.evidence.application.usecase;

import team.incude.gsmc.v2.domain.evidence.presentation.data.request.CreateDraftActivityEvidenceReqeust;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.CreateDraftEvidenceResponse;

public interface CreateDraftActivityEvidenceUseCase {
    CreateDraftEvidenceResponse execute(CreateDraftActivityEvidenceReqeust request);
}
