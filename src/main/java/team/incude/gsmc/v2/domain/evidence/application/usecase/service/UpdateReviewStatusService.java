package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incude.gsmc.v2.domain.evidence.application.port.EvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.usecase.UpdateReviewStatusUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UpdateReviewStatusService implements UpdateReviewStatusUseCase {

    private final EvidencePersistencePort evidencePersistencePort;


    @Override
    @Transactional
    public void execute(Long evidenceId, ReviewStatus reviewStatus) {
        Evidence evidence = evidencePersistencePort.findEvidenceById(evidenceId);

        Evidence newEvidence = createEvidence(evidence, reviewStatus);
        saveEvidence(newEvidence);
    }

    private Evidence createEvidence(Evidence evidence, ReviewStatus reviewStatus) {
        return Evidence.builder()
                .id(evidence.getId())
                .reviewStatus(reviewStatus)
                .score(evidence.getScore())
                .evidenceType(evidence.getEvidenceType())
                .createdAt(evidence.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private void saveEvidence(Evidence evidence) {
        evidencePersistencePort.saveEvidence(evidence);
    }
}
