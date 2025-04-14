package team.incude.gsmc.v2.domain.evidence.application.usecase;

import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetEvidencesResponse;

public interface FindEvidenceUseCase {
    GetEvidencesResponse execute();
    GetEvidencesResponse execute(String email);
}
