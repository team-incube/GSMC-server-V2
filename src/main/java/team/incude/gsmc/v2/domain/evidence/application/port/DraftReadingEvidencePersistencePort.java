package team.incude.gsmc.v2.domain.evidence.application.port;

import team.incude.gsmc.v2.domain.evidence.domain.DraftReadingEvidence;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.util.List;
import java.util.UUID;

@Port(direction = PortDirection.OUTBOUND)
public interface DraftReadingEvidencePersistencePort {
    DraftReadingEvidence saveDraftReadingEvidence(DraftReadingEvidence draftReadingEvidence);

    DraftReadingEvidence findDraftReadingEvidenceById(UUID draftId);

    void deleteDraftReadingEvidenceById(UUID draftId);

    List<DraftReadingEvidence> findAllDraftReadingEvidenceByEmail(String email);
}
