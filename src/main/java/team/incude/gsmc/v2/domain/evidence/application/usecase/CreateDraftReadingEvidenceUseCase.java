package team.incude.gsmc.v2.domain.evidence.application.usecase;

import team.incude.gsmc.v2.domain.evidence.presentation.data.request.CreateReadingEvidenceRequest;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.CreateDraftEvidenceResponse;

public interface CreateDraftReadingEvidenceUseCase {
    CreateDraftEvidenceResponse execute(CreateReadingEvidenceRequest request);
}
