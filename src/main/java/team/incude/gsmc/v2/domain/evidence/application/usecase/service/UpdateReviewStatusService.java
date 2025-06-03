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
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.global.event.ScoreUpdatedEvent;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 증빙자료의 검토 상태를 변경하는 유스케이스 구현 클래스입니다.
 * <p>{@link UpdateReviewStatusUseCase}를 구현하며, 검토 상태 변경에 따라 점수를 갱신하고 이벤트를 발행합니다.
 * <p>특정 유형의 증빙자료는 반려 시 점수를 초기화하고, 그 외에는 1점을 차감합니다.
 * <p>검토 상태가 변경되면 {@link ScoreUpdatedEvent}가 발행되어 프론트엔드에 반영됩니다.
 * @author suuuuuuminnnnnn
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UpdateReviewStatusService implements UpdateReviewStatusUseCase {

    private final EvidencePersistencePort evidencePersistencePort;
    private final ScorePersistencePort scorePersistencePort;
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 증빙자료의 검토 상태를 변경하고 점수를 갱신한 뒤 이벤트를 발행합니다.
     * @param evidenceId 검토 상태를 변경할 증빙자료 ID
     * @param reviewStatus 새로 지정할 검토 상태 (예: APPROVE, REJECT)
     */
    @Override
    public void execute(Long evidenceId, ReviewStatus reviewStatus) {
        Evidence evidence = evidencePersistencePort.findEvidenceByIdWithLock(evidenceId);
        Score score = evidence.getScore();

        Evidence newEvidence = createEvidence(evidence, reviewStatus);

        saveScore(evidence, reviewStatus);
        evidencePersistencePort.saveEvidence(newEvidence);
        applicationEventPublisher.publishEvent(new ScoreUpdatedEvent(score.getMember().getEmail()));
    }

    /**
     * 기존 증빙자료를 기반으로 검토 상태만 수정한 새 Evidence 객체를 생성합니다.
     * @param evidence 기존 증빙자료
     * @param reviewStatus 새 검토 상태
     * @return 변경된 Evidence 객체
     */
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

    /**
     * 검토 상태가 REJECT인 경우 점수를 갱신합니다.
     * <p>특정 유형의 경우 점수를 0으로 초기화하며, 그 외에는 1점 차감됩니다.
     * @param evidence 증빙자료
     * @param reviewStatus 새 검토 상태
     */
    private void saveScore(Evidence evidence, ReviewStatus reviewStatus) {
        if (reviewStatus.equals(ReviewStatus.REJECT)) {
            scorePersistencePort.saveScore(updateScore(evidence));
        }
    }

    /**
     * 증빙자료 유형에 따라 점수를 초기화하거나 1점 차감한 Score 객체를 반환합니다.
     * @param e 증빙자료
     * @return 갱신된 Score 객체
     */
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