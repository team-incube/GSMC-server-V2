package team.incude.gsmc.v2.domain.evidence.application.port;

import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.util.List;

@Port(direction = PortDirection.OUTBOUND)
public interface OtherEvidencePersistencePort {
    OtherEvidence saveOtherEvidence(OtherEvidence evidence);

    List<OtherEvidence> findOtherEvidenceByMember(Member member);

    OtherEvidence findOtherEvidenceById(Long evidenceId);

    List<OtherEvidence> findOtherEvidenceByMemberAndType(Member member, EvidenceType evidenceType);

    void deleteOtherEvidenceById(Long evidenceId);

    Boolean existsOtherEvidenceByEvidenceId(Long evidenceId);
}