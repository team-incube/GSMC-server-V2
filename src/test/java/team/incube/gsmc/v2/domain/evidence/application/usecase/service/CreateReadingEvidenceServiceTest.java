package team.incube.gsmc.v2.domain.evidence.application.usecase.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import team.incube.gsmc.v2.domain.evidence.application.port.ReadingEvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.domain.ReadingEvidence;
import team.incube.gsmc.v2.domain.member.domain.Member;
import team.incube.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incube.gsmc.v2.domain.score.domain.Category;
import team.incube.gsmc.v2.domain.score.domain.Score;
import team.incube.gsmc.v2.global.event.ScoreUpdatedEvent;
import team.incube.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("독서 증빙자료 등록 서비스 클래스의")
public class CreateReadingEvidenceServiceTest {

    @Mock
    private ReadingEvidencePersistencePort readingEvidencePersistencePort;

    @Mock
    private ScorePersistencePort scorePersistencePort;

    @Mock
    private CurrentMemberProvider currentMemberProvider;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private CreateReadingEvidenceService createReadingEvidenceService;

    @Nested
    @DisplayName("execute(title, author, page, content) 메서드는")
    class Describe_execute {

        @Nested
        @DisplayName("유효한 요청이 주어졌을 때")
        class Context_with_valid_request {

            @Test
            @DisplayName("독서 영역 증빙자료를 등록하고 점수를 저장한다")
            void it_creates_reading_evidence_and_updates_score () throws IOException {
                // given
                String categoryName = "HUMANITIES-READING";
                String title = "title";
                String author = "author";
                Integer page = 245;
                String content = "content";

                Member member = Member.builder()
                        .email("s24035@gsm.hs.kr")
                        .build();

                Category category = Category.builder()
                        .name(categoryName)
                        .maximumValue(6)
                        .build();

                Score score = Score.builder()
                        .value(1)
                        .category(category)
                        .member(member)
                        .build();

                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(scorePersistencePort.findScoreByCategoryNameAndMemberEmailWithLock(category.getName(), member.getEmail())).thenReturn(score);

                // when
                createReadingEvidenceService.execute(title, author, page, content, null);

                // then
                verify(scorePersistencePort).saveScore(any(Score.class));
                verify(readingEvidencePersistencePort).saveReadingEvidence(any(), any(ReadingEvidence.class));
                verify(applicationEventPublisher).publishEvent(argThat((Object event) ->
                        event instanceof ScoreUpdatedEvent &&
                                ((ScoreUpdatedEvent) event).email().equals(member.getEmail())
                ));
            }
        }
    }
}
