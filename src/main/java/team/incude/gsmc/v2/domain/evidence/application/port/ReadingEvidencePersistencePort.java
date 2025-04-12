package team.incude.gsmc.v2.domain.evidence.application.port;

import team.incude.gsmc.v2.domain.evidence.domain.ReadingEvidence;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.util.List;

@Port(direction = PortDirection.OUTBOUND)
public interface ReadingEvidencePersistencePort {
    List<ReadingEvidence> findReadingEvidenceByMember(Member member);

    ReadingEvidence findReadingEvidenceByEvidenceId(Long evidenceId);

    ReadingEvidence saveReadingEvidence(ReadingEvidence readingEvidence);

    List<ReadingEvidence> findReadingEvidenceByMemberAndTypeAndTitle(Member member);

    void deleteReadingEvidenceById(Long evidenceId);

    Boolean existsReadingEvidenceByEvidenceId(Long evidenceId);
}