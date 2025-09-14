package team.incube.gsmc.v2.domain.evidence.application.usecase;
import team.incube.gsmc.v2.domain.evidence.presentation.data.response.GetActivityEvidenceResponse;

public interface FindActivityEvidenceByEvidenceIdUseCase {
    GetActivityEvidenceResponse execute(Long id);
}
