package team.incube.gsmc.v2.domain.evidence.application.usecase.service;


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
import team.incube.gsmc.v2.domain.evidence.application.port.EvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.domain.Evidence;
import team.incube.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incube.gsmc.v2.domain.member.domain.Member;
import team.incube.gsmc.v2.domain.score.application.port.CategoryPersistencePort;
import team.incube.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incube.gsmc.v2.domain.score.domain.Category;
import team.incube.gsmc.v2.domain.score.domain.Score;
import team.incube.gsmc.v2.global.event.ScoreUpdatedEvent;
import team.incube.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("점수제 증빙자료 등록 서비스 클래스의")
public class CreateOtherScoringEvidenceServiceTest {

    @Mock
    private ScorePersistencePort scorePersistencePort;

    @Mock
    private CurrentMemberProvider currentMemberProvider;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private CategoryPersistencePort categoryPersistencePort;

    @Mock
    private OtherEvidencePersistencePort otherEvidencePersistencePort;

    @Mock
    private EvidencePersistencePort evidencePersistencePort;

    @InjectMocks
    private CreateOtherScoringEvidenceService createOtherScoringEvidenceService;

    @Nested
    @DisplayName("execute(categoryName, file, value)")
    class Describe_execute {

        @Nested
        @DisplayName("유효한 요청이 주어졌을 때")
        class Context_with_valid_request {

            @Test
            @DisplayName("점수제 증빙자료를 생성하고 점수를 저장한다")
            void it_create_other_scoring_evidence_and_updates_score () throws IOException {
                // given
                String categoryName = "FOREIGN_LANG-TOEIC_SCORE";
                String fakeFileUrl = "https://s3.url/evidence.png";
                Integer value = 550;

                MultipartFile file = new MockMultipartFile(
                        "file",
                        "evidence.png",
                        "application/png",
                        "test".getBytes()
                );
                Member member = Member.builder()
                        .email("s24035@gsm.hs.kr")
                        .build();

                Category category = Category.builder()
                        .name(categoryName)
                        .maximumValue(1000)
                        .build();

                Score score = Score.builder()
                        .value(value)
                        .category(category)
                        .member(member)
                        .build();

                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(scorePersistencePort.findScoreByCategoryNameAndMemberEmailWithLock(categoryName, member.getEmail()))
                        .thenReturn(score);
                when(scorePersistencePort.saveScore(any())).thenAnswer(invocation -> invocation.getArgument(0));
                when(evidencePersistencePort.saveEvidence(any())).thenAnswer(invocation -> invocation.getArgument(0));
                when(categoryPersistencePort.findAllCategory()).thenReturn(List.of(category));

                // when
                createOtherScoringEvidenceService.execute(categoryName, file, value);

                // then
                verify(scorePersistencePort).saveScore(argThat(savedScore -> savedScore.getValue() == 550));
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
