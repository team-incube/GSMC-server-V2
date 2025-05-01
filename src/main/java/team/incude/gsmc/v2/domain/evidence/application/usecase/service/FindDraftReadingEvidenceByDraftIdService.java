package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.evidence.application.port.ReadingEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.usecase.FindDraftReadingEvidenceByDraftIdUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.DraftReadingEvidence;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetDraftReadingEvidenceResponse;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindDraftReadingEvidenceByDraftIdService implements FindDraftReadingEvidenceByDraftIdUseCase {

    private final ReadingEvidencePersistencePort readingEvidencePersistencePort;

    @Override
    public GetDraftReadingEvidenceResponse execute(UUID draftId) {
        DraftReadingEvidence evidence = readingEvidencePersistencePort.findDraftReadingEvidenceById(draftId);

        return new GetDraftReadingEvidenceResponse(
                evidence.getId(),
                evidence.getTitle(),
                evidence.getAuthor(),
                evidence.getPage(),
                evidence.getContent()
        );
    }
}
