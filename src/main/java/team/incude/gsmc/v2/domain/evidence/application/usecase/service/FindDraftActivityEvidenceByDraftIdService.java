package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.usecase.FindDraftActivityEvidenceByDraftIdUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.DraftActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetDraftActivityEvidenceResponse;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindDraftActivityEvidenceByDraftIdService implements FindDraftActivityEvidenceByDraftIdUseCase {

    private final ActivityEvidencePersistencePort activityEvidencePersistencePort;

    @Override
    public GetDraftActivityEvidenceResponse execute(UUID draftId) {
        DraftActivityEvidence evidence = activityEvidencePersistencePort.findDraftActivityEvidenceById(draftId);

        return new GetDraftActivityEvidenceResponse(
                evidence.getId(),
                evidence.getCategoryName(),
                evidence.getTitle(),
                evidence.getContent(),
                evidence.getImageUrl(),
                evidence.getEvidenceType());
    }
}
