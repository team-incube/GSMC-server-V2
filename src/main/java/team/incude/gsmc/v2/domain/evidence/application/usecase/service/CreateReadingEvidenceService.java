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
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.score.application.port.CategoryPersistencePort;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Category;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.domain.score.exception.CategoryNotFoundException;
import team.incude.gsmc.v2.domain.score.exception.ScoreLimitExceededException;
import team.incude.gsmc.v2.global.event.DraftEvidenceDeleteEvent;
import team.incude.gsmc.v2.global.event.ScoreUpdatedEvent;
import team.incude.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;
import team.incude.gsmc.v2.global.util.ValueLimiterUtil;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 독서 증빙자료 생성을 처리하는 유스케이스 구현 클래스입니다.
 * <p>{@link CreateReadingEvidenceUseCase}를 구현하며, 제목, 저자, 페이지 수, 독서 내용, 임시저장 ID를 바탕으로
 * 새로운 독서 증빙자료를 생성하고 저장합니다.
 * <p>점수 누적 및 상한 검사, Evidence/ReadingEvidence 생성, 이벤트 발행 등의 작업을 수행합니다.
 * 점수가 존재하지 않을 경우 새로 생성되며, 초과 시 {@link ScoreLimitExceededException}을 발생시킵니다.
 * @author suuuuuuminnnnnn
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CreateReadingEvidenceService implements CreateReadingEvidenceUseCase {

    private final ReadingEvidencePersistencePort readingEvidencePersistencePort;
    private final ScorePersistencePort scorePersistencePort;
    private final CurrentMemberProvider currentMemberProvider;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final CategoryPersistencePort categoryPersistencePort;

    private final static String HUMANITIES_READING = "HUMANITIES-READING";

    /**
     * 독서 증빙자료를 생성하고 관련 점수를 갱신하며 이벤트를 발행합니다.
     * <p>점수가 없을 경우 새로 생성되며, 점수 상한 초과 시 예외가 발생합니다.
     * 생성된 증빙자료는 저장되고, {@link ScoreUpdatedEvent}와 {@link DraftEvidenceDeleteEvent}가 발행됩니다.
     * @param title 책 제목
     * @param author 저자명
     * @param page 페이지 수
     * @param content 독서 내용 요약
     * @param draftId 임시저장 ID (선택)
     * @throws ScoreLimitExceededException 점수가 최대치를 초과한 경우
     */
    @Override
    public void execute(String title, String author, int page, String content, UUID draftId) {
        Member member = currentMemberProvider.getCurrentUser();
        Score score = scorePersistencePort.findScoreByCategoryNameAndMemberEmailWithLock(HUMANITIES_READING, member.getEmail());

        if (score == null) {
            score = createScore(member);
        } else if (ValueLimiterUtil.isExceedingLimit(score.getValue() + 1, score.getCategory().getMaximumValue())) {
            throw new ScoreLimitExceededException();
        }

        score.plusValue(1);
        score = scorePersistencePort.saveScore(score);

        Evidence evidence = createEvidence(score);
        ReadingEvidence readingEvidence = createReadingEvidence(evidence, title, author, page, content);

        readingEvidencePersistencePort.saveReadingEvidence(evidence, readingEvidence);
        applicationEventPublisher.publishEvent(new ScoreUpdatedEvent(member.getEmail()));
        applicationEventPublisher.publishEvent(new DraftEvidenceDeleteEvent(draftId));
    }

    /**
     * 독서 점수가 존재하지 않을 경우 새 Score 객체를 생성합니다.
     * @param member 현재 사용자
     * @return 생성된 Score 객체
     */
    private Score createScore(Member member) {
        Category category = categoryPersistencePort.findAllCategory()
                .stream()
                .filter(cat -> cat.getName().equals(HUMANITIES_READING))
                .findFirst()
                .orElseThrow(CategoryNotFoundException::new);
        return Score.builder()
                .category(category)
                .value(0)
                .member(member)
                .build();
    }

    /**
     * 독서 증빙자료용 Evidence 객체를 생성합니다.
     * @param score 연관된 점수
     * @return 생성된 Evidence 객체
     */
    private Evidence createEvidence(Score score) {
        return Evidence.builder()
                .score(score)
                .reviewStatus(ReviewStatus.PENDING)
                .evidenceType(EvidenceType.READING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Evidence를 기반으로 독서 증빙자료 객체를 생성합니다.
     * @param evidence 연관 Evidence
     * @param title 책 제목
     * @param author 저자명
     * @param page 페이지 수
     * @param content 독서 내용 요약
     * @return 생성된 ReadingEvidence 객체
     */
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