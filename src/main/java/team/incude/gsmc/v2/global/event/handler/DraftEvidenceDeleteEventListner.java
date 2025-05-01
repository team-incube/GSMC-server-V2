package team.incude.gsmc.v2.global.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.ReadingEvidencePersistencePort;
import team.incude.gsmc.v2.global.event.DraftEvidenceDeleteEvent;

@Component
@RequiredArgsConstructor
public class DraftEvidenceDeleteEventListner{

    private final ActivityEvidencePersistencePort activityEvidencePersistencePort;
    private final ReadingEvidencePersistencePort readingEvidencePersistencePort;

    @Async
    @EventListener(DraftEvidenceDeleteEvent.class)
    public void handleDraftEvidenceDeleteEvent(DraftEvidenceDeleteEvent event) {
        activityEvidencePersistencePort.deleteDraftActivityEvidenceById(event.getDraftId());
        readingEvidencePersistencePort.deleteDraftReadingEvidenceById(event.getDraftId());
    }
}
