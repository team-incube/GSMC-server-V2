package team.incude.gsmc.v2.domain.evidence.application.port;

import team.incude.gsmc.v2.domain.evidence.domain.DraftActivityEvidence;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.util.UUID;

@Port(direction = PortDirection.OUTBOUND)
public interface DraftActivityEvidencePersistencePort {
    DraftActivityEvidence saveDraftActivityEvidence(DraftActivityEvidence draftActivityEvidence);

    DraftActivityEvidence findDraftActivityEvidenceById(UUID draftId);

    void deleteDraftActivityEvidenceById(UUID draftId);
}
