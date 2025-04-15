package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incude.gsmc.v2.domain.evidence.application.port.EvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.ReadingEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.usecase.UpdateReadingEvidenceByCurrentUserUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.ReadingEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.evidence.exception.ReadingEvidenceNotFoundException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UpdateReadingEvidenceByCurrentUserService implements UpdateReadingEvidenceByCurrentUserUseCase {

    private final EvidencePersistencePort evidencePersistencePort;
    private final ReadingEvidencePersistencePort readingEvidencePersistencePort;

    @Override
    @Transactional
    public void execute(Long evidenceId, String title, String author, String content, int page) {
        Evidence evidence = evidencePersistencePort.findEvidenceById(evidenceId);
        validateNotExistsReading(evidenceId);

        Evidence newEvidence = createEvidence(evidence);
        ReadingEvidence readingEvidence = createReadingEvidence(newEvidence, title, author, content, page);
        saveReadingEvidence(readingEvidence);
    }

    private void validateNotExistsReading(Long evidenceId) {
        if (readingEvidencePersistencePort.existsReadingEvidenceByEvidenceId(evidenceId)) {
            throw new ReadingEvidenceNotFoundException();
        }
    }

    private Evidence createEvidence(Evidence evidence) {
        return Evidence.builder()
                .id(evidence.getId())
                .score(evidence.getScore())
                .reviewStatus(ReviewStatus.PENDING)
                .evidenceType(evidence.getEvidenceType())
                .createdAt(evidence.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private ReadingEvidence createReadingEvidence(Evidence evidence, String title, String author, String content, int page) {
        return ReadingEvidence.builder()
                .id(evidence)
                .author(author)
                .title(title)
                .page(page)
                .content(content)
                .build();
    }

    private void saveReadingEvidence(ReadingEvidence readingEvidence) {
        readingEvidencePersistencePort.saveReadingEvidence(readingEvidence);
    }
}
