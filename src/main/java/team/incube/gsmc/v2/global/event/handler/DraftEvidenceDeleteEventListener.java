package team.incube.gsmc.v2.global.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import team.incube.gsmc.v2.domain.evidence.application.port.DraftActivityEvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.application.port.DraftReadingEvidencePersistencePort;
import team.incube.gsmc.v2.global.event.DraftEvidenceDeleteEvent;

/**
 * 임시 저장 증빙 삭제 이벤트를 처리하는 이벤트 리스너 클래스입니다.
 * <p>{@link DraftEvidenceDeleteEvent} 이벤트가 발생하면 활동 및 독서 증빙 초안을 삭제합니다.
 * 해당 동작은 비동기적으로 수행되며, 임시 저장 ID가 null인 경우는 무시됩니다.
 * @author suuuuuuminnnnnn
 */
@Component
@RequiredArgsConstructor
public class DraftEvidenceDeleteEventListener {

    private final DraftActivityEvidencePersistencePort activityEvidencePersistencePort;
    private final DraftReadingEvidencePersistencePort readingEvidencePersistencePort;

    @Async
    @TransactionalEventListener(value = DraftEvidenceDeleteEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void handleDraftEvidenceDeleteEvent(DraftEvidenceDeleteEvent event) {

        if (event.draftId() == null) {
            return;
        }

        activityEvidencePersistencePort.deleteDraftActivityEvidenceById(event.draftId());
        readingEvidencePersistencePort.deleteDraftReadingEvidenceById(event.draftId());
    }
}
