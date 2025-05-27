package team.incude.gsmc.v2.global.event.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.evidence.application.port.DraftActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.DraftReadingEvidencePersistencePort;
import team.incude.gsmc.v2.global.event.DraftEvidenceDeleteEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class DraftEvidenceDeleteEventListener {

    private final DraftActivityEvidencePersistencePort activityEvidencePersistencePort;
    private final DraftReadingEvidencePersistencePort readingEvidencePersistencePort;

    @Async
    @EventListener(DraftEvidenceDeleteEvent.class)
    public void handleDraftEvidenceDeleteEvent(DraftEvidenceDeleteEvent event) {
        try {
            activityEvidencePersistencePort.deleteDraftActivityEvidenceById(event.draftId());
        } catch (Exception e) {
            log.debug("Failed to delete draft activity evidence. draftId: {}, error: {}", event.draftId(), e.getMessage());
        }

        try {
            readingEvidencePersistencePort.deleteDraftReadingEvidenceById(event.draftId());
        } catch (Exception e) {
            log.debug("Failed to delete draft reading evidence. draftId: {}, error: {}", event.draftId(), e.getMessage());
        }
    }
}
