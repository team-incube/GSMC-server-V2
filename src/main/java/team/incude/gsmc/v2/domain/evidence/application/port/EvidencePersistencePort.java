package team.incude.gsmc.v2.domain.evidence.application.port;

import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

@Port(direction = PortDirection.OUTBOUND)
public interface EvidencePersistencePort {
    Evidence findEvidenceById(Long id);

    Evidence saveEvidence(Evidence evidence);

    void deleteEvidenceById(Long evidenceId);
}