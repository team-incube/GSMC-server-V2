package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incude.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.EvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.ReadingEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.usecase.DeleteEvidenceUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Score;

@Service
@RequiredArgsConstructor
public class DeleteEvidenceService implements DeleteEvidenceUseCase {

    private final EvidencePersistencePort evidencePersistencePort;
    private final ActivityEvidencePersistencePort activityEvidencePersistencePort;
    private final OtherEvidencePersistencePort otherEvidencePersistencePort;
    private final ReadingEvidencePersistencePort readingEvidencePersistencePort;
    private final ScorePersistencePort scorePersistencePort;

    @Override
    @Transactional
    public void execute(Long evidenceId) {
        Evidence evidence = evidencePersistencePort.findEvidenceById(evidenceId);
        Score score = evidence.getScore();
        score.minusValue(1);

        deleteSubEvidenceIfExists(evidence.getId());

        scorePersistencePort.saveScore(score);
        evidencePersistencePort.deleteEvidenceById(evidenceId);
    }

    private void deleteSubEvidenceIfExists(Long evidenceId) {
        if (activityEvidencePersistencePort.existsActivityEvidenceByEvidenceId(evidenceId)) {
            activityEvidencePersistencePort.deleteActivityEvidenceById(evidenceId);
        } else if (otherEvidencePersistencePort.existsOtherEvidenceByEvidenceId(evidenceId)) {
            otherEvidencePersistencePort.deleteOtherEvidenceById(evidenceId);
        } else if (readingEvidencePersistencePort.existsReadingEvidenceByEvidenceId(evidenceId)) {
            readingEvidencePersistencePort.deleteReadingEvidenceById(evidenceId);
        }
    }
}

