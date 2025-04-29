package team.incude.gsmc.v2.domain.evidence.application.port;

import team.incude.gsmc.v2.domain.evidence.domain.ReadingEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.util.List;

@Port(direction = PortDirection.OUTBOUND)
public interface ReadingEvidencePersistencePort {
    List<ReadingEvidence> findReadingEvidenceByEmail(String email);

    ReadingEvidence saveReadingEvidence(ReadingEvidence readingEvidence);

    List<ReadingEvidence> findReadingEvidenceByStudentCodeAndTitleAndTypeAndStatusAndGradeAndClassNumber(String studentCode, String title, EvidenceType evidenceType, ReviewStatus status, Integer grade, Integer classNumber);

    void deleteReadingEvidenceById(Long evidenceId);

    ReadingEvidence findReadingEvidenceById(Long id);
}