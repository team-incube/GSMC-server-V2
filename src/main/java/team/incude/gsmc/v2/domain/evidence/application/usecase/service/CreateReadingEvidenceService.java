package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incude.gsmc.v2.domain.evidence.application.port.ReadingEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.usecase.CreateReadingEvidenceUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.ReadingEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.member.domain.StudentDetail;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.global.event.ScoreUpdatedEvent;
import team.incude.gsmc.v2.global.security.jwt.usecase.service.CurrentMemberProvider;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateReadingEvidenceService implements CreateReadingEvidenceUseCase {

    private final ReadingEvidencePersistencePort readingEvidencePersistencePort;
    private final StudentDetailPersistencePort studentDetailPersistencePort;
    private final ScorePersistencePort scorePersistencePort;
    private final CurrentMemberProvider currentMemberProvider;
    private final ApplicationEventPublisher applicationEventPublisher;

    private final static String HUMANITIES_READING = "HUMANITIES-READING";

    @Override
    public void execute(String title, String author, int page, String content) {
        Member member = currentMemberProvider.getCurrentUser();
        StudentDetail studentDetail = studentDetailPersistencePort.findStudentDetailByMemberEmail(member.getEmail());
        Score score = scorePersistencePort.findScoreByCategoryNameAndStudentDetailStudentCodeWithLock(HUMANITIES_READING, studentDetail.getStudentCode());

        score.plusValue(1);

        Evidence evidence = createEvidence(score);
        ReadingEvidence readingEvidence = createReadingEvidence(evidence, title, author, page, content);

        scorePersistencePort.saveScore(score);
        readingEvidencePersistencePort.saveReadingEvidence(readingEvidence);
        applicationEventPublisher.publishEvent(new ScoreUpdatedEvent(studentDetail.getStudentCode()));
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
}
