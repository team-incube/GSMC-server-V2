package team.incude.gsmc.v2.domain.evidence.application.usecase;

import team.incude.gsmc.v2.domain.evidence.presentation.data.response.CreateDraftEvidenceResponse;

import java.util.UUID;

public interface CreateDraftReadingEvidenceUseCase {
    CreateDraftEvidenceResponse execute(UUID draftId, String title, String author, Integer page, String content);
}
