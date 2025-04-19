package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Category;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.global.security.jwt.usecase.service.CurrentMemberProvider;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateActivityEvidenceService의")
class CreateActivityEvidenceServiceTest {

    @Mock
    private ActivityEvidencePersistencePort activityEvidencePersistencePort;

    @Mock
    private ScorePersistencePort scorePersistencePort;

    @Mock
    private S3Port s3Port;

    @Mock
    private CurrentMemberProvider currentMemberProvider;

    @InjectMocks
    private CreateActivityEvidenceService createActivityEvidenceService;

    @Nested
    @DisplayName("execute 메서드는")
    class Describe_execute {

        @Nested
        @DisplayName("유효한 요청이 주어졌을 때")
        class Context_with_valid_request {

            @Test
            @DisplayName("활동 증빙을 생성한다.")
            void it_creates_activity_evidence() throws Exception {
                // given
                String categoryName = "동아리활동";
                String title = "백엔드 세미나";
                String content = "JPA 소개 세션 진행";
                EvidenceType activityType = EvidenceType.MAJOR;

                Member member = Member.builder()
                        .email("test@example.com")
                        .build();

                Category category = Category.builder()
                        .name(categoryName)
                        .build();

                Score score = Score.builder()
                        .id(1L)
                        .member(member)
                        .category(category)
                        .value(0)
                        .build();

                MultipartFile file = mock(MultipartFile.class);
                InputStream fakeInputStream = mock(InputStream.class);

                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(scorePersistencePort.findScoreByCategoryNameAndMemberEmail(categoryName, member.getEmail()))
                        .thenReturn(score);
                when(file.isEmpty()).thenReturn(false);
                when(file.getInputStream()).thenReturn(fakeInputStream);
                when(s3Port.uploadFile(anyString(), eq(fakeInputStream)))
                        .thenReturn(CompletableFuture.completedFuture("https://s3.com/fake.png"));

                // when
                createActivityEvidenceService.execute(categoryName, title, content, file, activityType);

                // then
                verify(scorePersistencePort).saveScore(any());
                verify(activityEvidencePersistencePort).saveActivityEvidence(any());
            }
        }
    }
}
