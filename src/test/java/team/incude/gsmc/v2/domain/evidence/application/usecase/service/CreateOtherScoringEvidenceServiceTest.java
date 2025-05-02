package team.incude.gsmc.v2.domain.evidence.application.usecase.service;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.member.domain.StudentDetail;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Category;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.global.event.ScoreUpdatedEvent;
import team.incude.gsmc.v2.global.security.jwt.usecase.service.CurrentMemberProvider;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("점수제 증빙자료 등록 서비스 클래스의")
public class CreateOtherScoringEvidenceServiceTest {

    @Mock
    private S3Port s3Port;

    @Mock
    private StudentDetailPersistencePort studentDetailPersistencePort;

    @Mock
    private ScorePersistencePort scorePersistencePort;

    @Mock
    private CurrentMemberProvider currentMemberProvider;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private OtherEvidencePersistencePort otherEvidencePersistencePort;

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
                        .value(value)
                        .category(category)
                        .member(member)
                        .build();

                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(studentDetailPersistencePort.findStudentDetailByMemberEmail(member.getEmail())).thenReturn(studentDetail);
                when(scorePersistencePort.findScoreByCategoryNameAndStudentDetailStudentCodeWithLock(category.getName(), studentDetail.getStudentCode())).thenReturn(score);
                when(s3Port.uploadFile(Mockito.anyString(), Mockito.any()))
                        .thenReturn(CompletableFuture.completedFuture(fakeFileUrl));
                // when
                createOtherScoringEvidenceService.execute(categoryName, file, value);

                // then
                verify(scorePersistencePort).saveScore(any(Score.class));
                verify(otherEvidencePersistencePort).saveOtherEvidence(any(OtherEvidence.class));
                verify(applicationEventPublisher).publishEvent(argThat((Object event) ->
                        event instanceof ScoreUpdatedEvent &&
                                ((ScoreUpdatedEvent) event).studentCode().equals(studentDetail.getStudentCode())
                ));
            }
        }
    }
}
