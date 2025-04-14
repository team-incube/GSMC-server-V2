package team.incude.gsmc.v2.domain.evidence.application.port;

import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.util.List;

@Port(direction = PortDirection.OUTBOUND)
public interface OtherEvidencePersistencePort {
    OtherEvidence saveOtherEvidence(OtherEvidence evidence);

    List<OtherEvidence> findOtherEvidenceByEmail(String email);

    OtherEvidence findOtherEvidenceById(Long evidenceId);

    void deleteOtherEvidenceById(Long evidenceId);

    Boolean existsOtherEvidenceByEvidenceId(Long evidenceId);
}