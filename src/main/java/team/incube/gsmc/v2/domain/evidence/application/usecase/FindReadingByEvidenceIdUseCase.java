package team.incube.gsmc.v2.domain.evidence.application.usecase;

import team.incube.gsmc.v2.domain.evidence.presentation.data.response.GetReadingEvidenceResponse;

public interface FindReadingByEvidenceIdUseCase {
    GetReadingEvidenceResponse execute(Long id);
}
