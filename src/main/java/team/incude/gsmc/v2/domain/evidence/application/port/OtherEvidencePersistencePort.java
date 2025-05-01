package team.incude.gsmc.v2.domain.evidence.application.port;

import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.util.List;

@Port(direction = PortDirection.OUTBOUND)
public interface OtherEvidencePersistencePort {
    OtherEvidence saveOtherEvidence(OtherEvidence evidence);

    List<OtherEvidence> findOtherEvidenceByEmail(String email);

    List<OtherEvidence> searchOtherEvidence(String studentCode, EvidenceType evidenceType, ReviewStatus status, Integer grade, Integer classNumber);

    void deleteOtherEvidenceById(Long evidenceId);

    OtherEvidence findOtherEvidenceById(Long id);
}