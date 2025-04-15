package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incude.gsmc.v2.domain.evidence.application.port.ReadingEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.usecase.CreateReadingEvidenceUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.ReadingEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.global.security.jwt.service.CurrentMemberProvider;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateReadingEvidenceService implements CreateReadingEvidenceUseCase {

    private final ReadingEvidencePersistencePort readingEvidencePersistencePort;
    private final ScorePersistencePort scorePersistencePort;
    private final CurrentMemberProvider currentMemberProvider;

    private final static String HUMANITIES_BOOK_SCHOOL_DESIGNATED_BOOK_CATEGORY_NAME = "humanities-book-school_designated_book";


    @Override
    @Transactional
    public void execute(String title, String author, int page, String content) {
        Member member = currentMemberProvider.getCurrentUser();
        Score score = scorePersistencePort.findScoreByCategoryNameAndMemberEmail(HUMANITIES_BOOK_SCHOOL_DESIGNATED_BOOK_CATEGORY_NAME, member.getEmail());

        Evidence newEvidence = createEvidence(score);
        ReadingEvidence newReadingEvidence = createReadingEvidence(newEvidence, title, author, page, content);
        saveReadingEvidence(newReadingEvidence);
    }

    private Evidence createEvidence(Score score) {
        return Evidence.builder()
                .score(score)
                .reviewStatus(ReviewStatus.PENDING)
                .evidenceType(EvidenceType.READING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private ReadingEvidence createReadingEvidence(Evidence evidence, String title, String author, int page, String content) {
        return ReadingEvidence.builder()
                .id(evidence)
                .title(title)
                .author(author)
                .page(page)
                .content(content)
                .build();
    }

    private void saveReadingEvidence(ReadingEvidence readingEvidence) {
        readingEvidencePersistencePort.saveReadingEvidence(readingEvidence);
    }
}