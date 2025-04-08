package team.incude.gsmc.v2.domain.evidence.application.port;

import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.util.List;

@Port(direction = PortDirection.OUTBOUND)
public interface EvidencePersistencePort {
    List<Evidence> findEvidencesByEmail(String email);

    void deleteEvidenceByEvidenceId(Long evidenceId);

    Evidence findEvidenceById(Long id);

    Evidence saveEvidence(Evidence evidence);
}