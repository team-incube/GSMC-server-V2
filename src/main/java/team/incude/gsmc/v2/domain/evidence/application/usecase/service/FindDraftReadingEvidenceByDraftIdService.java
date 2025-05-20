package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.evidence.application.port.DraftReadingEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.usecase.FindDraftReadingEvidenceByDraftIdUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.DraftReadingEvidence;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetDraftReadingEvidenceResponse;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindDraftReadingEvidenceByDraftIdService implements FindDraftReadingEvidenceByDraftIdUseCase {

    private final DraftReadingEvidencePersistencePort draftReadingEvidencePersistencePort;

    @Override
    public GetDraftReadingEvidenceResponse execute(UUID draftId) {
        DraftReadingEvidence evidence = draftReadingEvidencePersistencePort.findDraftReadingEvidenceById(draftId);

        return new GetDraftReadingEvidenceResponse(
                evidence.getId(),
                evidence.getTitle(),
                evidence.getAuthor(),
                evidence.getPage(),
                evidence.getContent()
        );
    }
}
