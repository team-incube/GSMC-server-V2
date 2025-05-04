package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import team.incude.gsmc.v2.domain.evidence.application.port.ReadingEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.domain.ReadingEvidence;
import team.incude.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.member.domain.StudentDetail;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Category;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.global.event.ScoreUpdatedEvent;
import team.incude.gsmc.v2.global.security.jwt.usecase.service.CurrentMemberProvider;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("독서 증빙자료 등록 서비스 클래스의")
public class CreateReadingEvidenceServiceTest {

    @Mock
    private ReadingEvidencePersistencePort readingEvidencePersistencePort;

    @Mock
    private StudentDetailPersistencePort studentDetailPersistencePort;

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
                        .email("test@gsm.hs.kr")
                        .build();

                StudentDetail studentDetail = StudentDetail.builder()
                        .studentCode("1234")
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
                when(studentDetailPersistencePort.findStudentDetailByMemberEmail(member.getEmail())).thenReturn(studentDetail);
                when(scorePersistencePort.findScoreByCategoryNameAndStudentDetailStudentCodeWithLock(category.getName(), studentDetail.getStudentCode())).thenReturn(score);

                // when
                createReadingEvidenceService.execute(title, author, page, content);

                // then
                verify(scorePersistencePort).saveScore(any(Score.class));
                verify(readingEvidencePersistencePort).saveReadingEvidence(any(ReadingEvidence.class));
                verify(applicationEventPublisher).publishEvent(argThat((Object event) ->
                        event instanceof ScoreUpdatedEvent &&
                                ((ScoreUpdatedEvent) event).studentCode().equals(studentDetail.getStudentCode())
                ));
            }
        }
    }
}
