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
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.global.event.ScoreUpdatedEvent;
import team.incude.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;

import java.util.Set;

/**
 * 증빙자료 삭제를 처리하는 유스케이스 구현 클래스입니다.
 * <p>{@link DeleteEvidenceUseCase}를 구현하며, 증빙자료 ID를 기반으로 활동/독서/기타 증빙자료를 삭제하고
 * 관련 점수를 업데이트합니다.
 * <p>첨부 파일이 존재하는 경우 S3에서 해당 파일을 삭제하며,
 * 점수가 존재하고 증빙자료가 반려되지 않은 경우에는 점수 차감 또는 초기화가 수행됩니다.
 * <p>삭제 이후 {@link team.incude.gsmc.v2.global.event.ScoreUpdatedEvent}를 발행하여 클라이언트에 점수 변경을 알립니다.
 * @author suuuuuuminnnnnn
 */
@Service
@Transactional
@RequiredArgsConstructor
public class DeleteEvidenceService implements DeleteEvidenceUseCase {

    private final EvidencePersistencePort evidencePersistencePort;
    private final ActivityEvidencePersistencePort activityEvidencePersistencePort;
    private final OtherEvidencePersistencePort otherEvidencePersistencePort;
    private final ReadingEvidencePersistencePort readingEvidencePersistencePort;
    private final ScorePersistencePort scorePersistencePort;
    private final CurrentMemberProvider currentMemberProvider;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final S3Port s3Port;

    /**
     * 증빙자료를 삭제하고, 관련 점수를 갱신한 후 이벤트를 발행합니다.
     * <p>증빙자료 ID를 기준으로 활동/독서/기타 타입의 하위 데이터를 삭제하며,
     * 점수가 존재하고 해당 자료가 반려되지 않았을 경우 점수 차감 또는 초기화 후 저장합니다.
     * @param evidenceId 삭제할 증빙자료 ID
     */
    @Override
    public void execute(Long evidenceId) {
        Evidence evidence = evidencePersistencePort.findEvidenceById(evidenceId);
        Member member = currentMemberProvider.getCurrentUser();

        deleteSubEvidenceIfExists(evidence.getId());

        saveScore(evidence);
        evidencePersistencePort.deleteEvidenceById(evidenceId);
        applicationEventPublisher.publishEvent(new ScoreUpdatedEvent(member.getEmail()));
    }

    /**
     * 증빙자료 ID에 해당하는 활동, 기타, 독서 증빙자료를 찾아 삭제합니다.
     * <p>첨부된 파일 또는 이미지가 존재하는 경우 S3에서도 삭제합니다.
     * @param evidenceId 삭제할 증빙자료 ID
     */
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

    /**
     * 증빙자료가 반려되지 않은 경우 점수를 갱신하여 저장합니다.
     * @param evidence 대상 증빙자료
     */
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

    /**
     * 증빙자료 타입에 따라 점수를 0으로 초기화하거나 1점 차감합니다.
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