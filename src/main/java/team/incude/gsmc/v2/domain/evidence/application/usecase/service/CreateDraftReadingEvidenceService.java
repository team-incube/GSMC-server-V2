package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incude.gsmc.v2.domain.evidence.application.port.ReadingEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.usecase.CreateDraftReadingEvidenceUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.DraftReadingEvidence;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.CreateDraftEvidenceResponse;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateDraftReadingEvidenceService implements CreateDraftReadingEvidenceUseCase {

    private static final Long DRAFT_TTL_SECONDS = 7 * 24 * 60 * 60L;

    private final ReadingEvidencePersistencePort readingEvidencePersistencePort;

    @Override
    public CreateDraftEvidenceResponse execute(UUID id, String title, String author, Integer page, String content) {
        UUID draftId = id == null ? UUID.randomUUID() : id;

        DraftReadingEvidence draftReadingEvidence = createReadingEvidence(draftId, title, page, author, content);

        readingEvidencePersistencePort.saveDraftReadingEvidence(draftReadingEvidence);
        return new CreateDraftEvidenceResponse(draftId);
    }

    private DraftReadingEvidence createReadingEvidence(UUID id, String title, Integer page, String author, String content) {
        return DraftReadingEvidence.builder()
                .id(id)
                .title(title)
                .page(page)
                .author(author)
                .content(content)
                .ttl(DRAFT_TTL_SECONDS)
                .build();
    }
}
