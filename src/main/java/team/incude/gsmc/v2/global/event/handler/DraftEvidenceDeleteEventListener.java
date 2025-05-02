package team.incude.gsmc.v2.global.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.evidence.application.port.DraftActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.DraftReadingEvidencePersistencePort;
import team.incude.gsmc.v2.global.event.DraftEvidenceDeleteEvent;

@Component
@RequiredArgsConstructor
public class DraftEvidenceDeleteEventListener {

    private final DraftActivityEvidencePersistencePort activityEvidencePersistencePort;
    private final DraftReadingEvidencePersistencePort readingEvidencePersistencePort;

    @Async
    @EventListener(DraftEvidenceDeleteEvent.class)
    public void handleDraftEvidenceDeleteEvent(DraftEvidenceDeleteEvent event) {
        activityEvidencePersistencePort.deleteDraftActivityEvidenceById(event.getDraftId());
        readingEvidencePersistencePort.deleteDraftReadingEvidenceById(event.getDraftId());
    }
}
