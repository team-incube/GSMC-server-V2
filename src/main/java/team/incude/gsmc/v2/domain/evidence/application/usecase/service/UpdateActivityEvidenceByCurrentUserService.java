package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.EvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.usecase.UpdateActivityEvidenceByCurrentUserUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;

@Service
@RequiredArgsConstructor
public class UpdateActivityEvidenceByCurrentUserService implements UpdateActivityEvidenceByCurrentUserUseCase {

    private final EvidencePersistencePort evidencePersistencePort;
    private final ActivityEvidencePersistencePort activityEvidencePersistencePort;

    @Override
    @Transactional
    public void execute(Long evidenceId, String title, String content, MultipartFile file, EvidenceType evidenceType) {
        Evidence evidence = evidencePersistencePort.findEvidenceById(evidenceId);
        ActivityEvidence activityEvidence = activityEvidencePersistencePort.findActivityEvidenceById(evidenceId);
    }
}
