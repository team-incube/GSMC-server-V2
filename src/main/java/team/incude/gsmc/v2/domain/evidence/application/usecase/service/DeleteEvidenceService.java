package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incude.gsmc.v2.domain.evidence.application.port.*;
import team.incude.gsmc.v2.domain.evidence.application.usecase.DeleteEvidenceUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.ReadingEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.member.domain.StudentDetail;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.global.event.ScoreUpdatedEvent;
import team.incude.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;

import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteEvidenceService implements DeleteEvidenceUseCase {

    private final EvidencePersistencePort evidencePersistencePort;
    private final ActivityEvidencePersistencePort activityEvidencePersistencePort;
    private final OtherEvidencePersistencePort otherEvidencePersistencePort;
    private final ReadingEvidencePersistencePort readingEvidencePersistencePort;
    private final StudentDetailPersistencePort studentDetailPersistencePort;
    private final ScorePersistencePort scorePersistencePort;
    private final CurrentMemberProvider currentMemberProvider;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final S3Port s3Port;

    @Override
    public void execute(Long evidenceId) {
        Evidence evidence = evidencePersistencePort.findEvidenceById(evidenceId);
        Member member = currentMemberProvider.getCurrentUser();
        StudentDetail studentDetail = studentDetailPersistencePort.findStudentDetailByMemberEmail(member.getEmail());

        deleteSubEvidenceIfExists(evidence.getId());

        saveScore(evidence);
        evidencePersistencePort.deleteEvidenceById(evidenceId);
        applicationEventPublisher.publishEvent(new ScoreUpdatedEvent(studentDetail.getStudentCode()));
    }

    private void deleteSubEvidenceIfExists(Long evidenceId) {

        ActivityEvidence activityEvidence = activityEvidencePersistencePort.findActivityEvidenceById(evidenceId);
        if (activityEvidence != null) {
            if (activityEvidence.getImageUrl() != null) {
                s3Port.deleteFile(activityEvidence.getImageUrl());
            }
            activityEvidencePersistencePort.deleteActivityEvidenceById(evidenceId);
            return;
        }

        OtherEvidence otherEvidence = otherEvidencePersistencePort.findOtherEvidenceById(evidenceId);
        if (otherEvidence != null) {
            if (otherEvidence.getFileUri() != null) {
                s3Port.deleteFile(otherEvidence.getFileUri());
            }
            activityEvidencePersistencePort.deleteActivityEvidenceById(evidenceId);
            return;
        }

        ReadingEvidence readingEvidence = readingEvidencePersistencePort.findReadingEvidenceById(evidenceId);
        if (readingEvidence != null) {
            readingEvidencePersistencePort.deleteReadingEvidenceById(evidenceId);
        }
    }

    private void saveScore(Evidence evidence) {
        if (!evidence.getReviewStatus().equals(ReviewStatus.REJECT)) {
            scorePersistencePort.saveScore(updateScore(evidence));
        }
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

