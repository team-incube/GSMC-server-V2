package team.incude.gsmc.v2.domain.evidence.application.usecase;

import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetDraftReadingEvidenceResponse;

import java.util.UUID;

public interface FindDraftReadingEvidenceByDraftIdUseCase {
    GetDraftReadingEvidenceResponse execute(UUID draftId);
}
