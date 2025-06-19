package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.application.port.EvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.score.application.port.CategoryPersistencePort;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Category;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.global.event.FileUploadEvent;
import team.incude.gsmc.v2.global.event.ScoreUpdatedEvent;
import team.incude.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("기타 증빙자료 생성 서비스 클래스의")
public class CreateOtherEvidenceServiceTest {

    @Mock
    private OtherEvidencePersistencePort otherEvidencePersistencePort;

    @Mock
    private ScorePersistencePort scorePersistencePort;

    @Mock
    private EvidencePersistencePort evidencePersistencePort;

    @Mock
    private CurrentMemberProvider currentMemberProvider;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private CreateOtherEvidenceService createOtherEvidenceService;

    @Nested
    @DisplayName("execute(categoryName, file) 메서드는")
    class Describe_execute {

        @Nested
        @DisplayName("유효한 요청이 주어졌을 때")
        class Context_with_valid_request {

            @Test
            @DisplayName("기타 증빙자료를 생성하고 점수를 갱신한다")
            void it_creates_other_evidence_and_updates_score() throws IOException {
                // given
                String categoryName = "HUMANITIES-READING-READ_A_THON-TURTLE";

                MultipartFile file = new MockMultipartFile(
                        "file",
                        "evidence.png",
                        "image/png",
                        "test".getBytes()
                );

                Member member = Member.builder()
                        .email("s24035@gsm.hs.kr")
                        .build();

                Category category = Category.builder()
                        .name(categoryName)
                        .maximumValue(6)
                        .build();

                Score score = Score.builder()
                        .value(2)
                        .category(category)
                        .member(member)
                        .build();

                // mocking
                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(scorePersistencePort.findScoreByCategoryNameAndMemberEmailWithLock(categoryName, member.getEmail()))
                        .thenReturn(score);
                when(scorePersistencePort.saveScore(any())).thenAnswer(invocation -> invocation.getArgument(0));
                when(evidencePersistencePort.saveEvidence(any())).thenAnswer(invocation -> invocation.getArgument(0));

                // when
                createOtherEvidenceService.execute(categoryName, file);

                // then
                verify(scorePersistencePort).saveScore(argThat(savedScore -> savedScore.getValue() == 3));
                verify(evidencePersistencePort).saveEvidence(any(Evidence.class));
                verify(otherEvidencePersistencePort).saveOtherEvidence(any(), any(OtherEvidence.class));
                verify(applicationEventPublisher).publishEvent(argThat((Object event) ->
                        event instanceof ScoreUpdatedEvent &&
                                ((ScoreUpdatedEvent) event).email().equals(member.getEmail())
                ));
            }
        }
    }
}
