package team.incude.gsmc.v2.domain.evidence.application.port;

import team.incude.gsmc.v2.domain.evidence.domain.ReadingEvidence;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.util.List;

@Port(direction = PortDirection.OUTBOUND)
public interface ReadingEvidencePersistencePort {
    List<ReadingEvidence> findReadingEvidenceByEmail(String email);

    ReadingEvidence findReadingEvidenceByEvidenceId(Long evidenceId);

    ReadingEvidence saveReadingEvidence(ReadingEvidence readingEvidence);

    List<ReadingEvidence> findReadingEvidenceByEmailAndTitle(String email, String title);

    void deleteReadingEvidenceById(Long evidenceId);

    Boolean existsReadingEvidenceByEvidenceId(Long evidenceId);
}