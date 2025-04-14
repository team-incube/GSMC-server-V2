package team.incude.gsmc.v2.domain.evidence.application.usecase;

import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetEvidencesResponse;

public interface FindEvidenceByEmailUseCase {
    GetEvidencesResponse execute(String email);
}
