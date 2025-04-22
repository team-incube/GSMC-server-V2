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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateOtherEvidenceServiceTest의")
public class CreateOtherEvidenceServiceTest {

    @Mock
    private S3Port s3Port;

    @Mock
    private StudentDetailPersistencePort studentDetailPersistencePort;

    @Mock
    private OtherEvidencePersistencePort otherEvidencePersistencePort;

    @Mock
    private ScorePersistencePort scorePersistencePort;

    @Mock
    private CurrentMemberProvider currentMemberProvider;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private CreateOtherEvidenceService createOtherEvidenceService;

    @Nested
    @DisplayName("execute(categoryName, file) 메서드는")
    class Describe_execute {

        @Test
        @DisplayName("기타 증빙자료를 등록하고 점수를 저장한다")
        void it_creates_other_evidence_and_updates_score() throws IOException {
            // given
            String categoryName = "MAJOR-AWARD_CAREER-OUT_SCHOOL-OFFICIAL";
            String fakeFileUrl = "https://s3.url/evidence.png";

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
                    .value(2)
                    .category(category)
                    .member(member)
                    .build();

            when(currentMemberProvider.getCurrentUser()).thenReturn(member);
            when(studentDetailPersistencePort.findStudentDetailByMemberEmail(member.getEmail())).thenReturn(studentDetail);
            when(scorePersistencePort.findScoreByCategoryNameAndMemberEmail(category.getName(), member.getEmail()))
                    .thenReturn(score);
            when(s3Port.uploadFile(Mockito.anyString(), Mockito.any()))
                    .thenReturn(CompletableFuture.completedFuture(fakeFileUrl));

            // when
            createOtherEvidenceService.execute(categoryName, file);

            // then
            verify(scorePersistencePort).saveScore(Mockito.any(Score.class));
            verify(otherEvidencePersistencePort).saveOtherEvidence(Mockito.any(OtherEvidence.class));
            verify(applicationEventPublisher).publishEvent(argThat((Object event) ->
                    event instanceof ScoreUpdatedEvent &&
                            ((ScoreUpdatedEvent) event).getStudentCode().equals(studentDetail.getStudentCode())
            ));
        }
    }
}
