package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incude.gsmc.v2.domain.evidence.application.port.EvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.usecase.UpdateReviewStatusUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.StudentDetail;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.global.event.ScoreUpdatedEvent;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateReviewStatusService implements UpdateReviewStatusUseCase {

    private final StudentDetailPersistencePort studentDetailPersistencePort;
    private final EvidencePersistencePort evidencePersistencePort;
    private final ScorePersistencePort scorePersistencePort;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void execute(Long evidenceId, ReviewStatus reviewStatus) {
        Evidence evidence = evidencePersistencePort.findEvidenceByIdWithLock(evidenceId);
        Score score = evidence.getScore();
        StudentDetail studentDetail = studentDetailPersistencePort.findStudentDetailByMemberEmail(score.getMember().getEmail());

        Evidence newEvidence = createEvidence(evidence, reviewStatus);

        saveScore(evidence, reviewStatus);
        evidencePersistencePort.saveEvidence(newEvidence);
        applicationEventPublisher.publishEvent(new ScoreUpdatedEvent(studentDetail.getStudentCode()));
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

    private static final Set<EvidenceType> RESET_SCORE_EVIDENCE_TYPES = Set.of(
            EvidenceType.TOEIC,
            EvidenceType.TOEFL,
            EvidenceType.TEPS,
            EvidenceType.TOEIC_SPEAKING,
            EvidenceType.OPIC,
            EvidenceType.JPT,
            EvidenceType.CPT,
            EvidenceType.HSK,
            EvidenceType.TOPCIT
    );

    private void saveScore(Evidence evidence, ReviewStatus reviewStatus) {
        if (reviewStatus.equals(ReviewStatus.REJECT)) {
            scorePersistencePort.saveScore(updateScore(evidence));
        }
    }

    private Score updateScore(Evidence e) {
        if (RESET_SCORE_EVIDENCE_TYPES.contains(e.getEvidenceType())) {
            return Score.builder()
                    .id(e.getScore().getId())
                    .member(e.getScore().getMember())
                    .value(0)
                    .category(e.getScore().getCategory())
                    .build();
        } else {
            e.getScore().minusValue(1);
            return e.getScore();
        }
    }
}
