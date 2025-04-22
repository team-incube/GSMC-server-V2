package team.incude.gsmc.v2.domain.evidence.application.usecase;

import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetEvidencesResponse;

public interface FindEvidenceByFilteringByStudentCodeAndTitleAndTypeUseCase {
    GetEvidencesResponse execute(String studentCode, String title, EvidenceType evidenceType);
}
