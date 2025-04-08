package team.incude.gsmc.v2.domain.evidence.application.port;

import team.incude.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.util.List;

@Port(direction = PortDirection.OUTBOUND)
public interface ActivityEvidencePersistencePort {
    List<ActivityEvidence> findActivityEvidenceByMemberAndEvidenceType(Member member, EvidenceType evidenceType);

    ActivityEvidence findActivityEvidenceByEvidenceIdAndEvidenceType(Long evidenceId, EvidenceType evidenceType);

    void saveActivityEvidence(ActivityEvidence activityEvidence);

    void deleteActivityEvidenceByEvidenceId(Long evidenceId);
}
