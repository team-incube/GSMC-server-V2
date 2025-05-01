package team.incude.gsmc.v2.domain.evidence.application.usecase;

import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetDraftActivityEvidenceResponse;

import java.util.UUID;

public interface FindDraftActivityEvidenceByDraftIdUseCase {
    GetDraftActivityEvidenceResponse execute(UUID draftId);
}
