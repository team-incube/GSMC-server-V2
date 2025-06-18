package team.incude.gsmc.v2.global.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import team.incude.gsmc.v2.domain.evidence.application.port.EvidenceApplicationPort;
import team.incude.gsmc.v2.global.event.FileUploadEvent;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Component
@RequiredArgsConstructor
public class FileUploadEventListener {

    private final EvidenceApplicationPort evidenceApplicationPort;

    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void handleFileUploadEvent(FileUploadEvent event) {
        evidenceApplicationPort.updateEvidenceFile(event.evidenceId(), event.fileName(), event.inputStream(), event.evidenceType());
    }
}
