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

    List<ActivityEvidence> findActivityEvidenceByMemberAndTypeAndTitle(Member member, EvidenceType evidenceType, String title);

    ActivityEvidence saveActivityEvidence(ActivityEvidence activityEvidence);

    void deleteActivityEvidenceById(Long evidenceId);

    Boolean existsActivityEvidenceByEvidenceId(Long evidenceId);
}
