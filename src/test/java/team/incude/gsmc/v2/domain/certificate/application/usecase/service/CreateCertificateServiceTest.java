package team.incude.gsmc.v2.domain.certificate.application.usecase.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;
import org.mockito.junit.jupiter.MockitoExtension;
import team.incude.gsmc.v2.domain.certificate.application.port.CertificatePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.score.application.port.CategoryPersistencePort;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Category;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.global.security.jwt.usecase.service.CurrentMemberProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("자격증 정보 등록 서비스 클래스의")
class CreateCertificateServiceTest {

    @Mock
    private CertificatePersistencePort certificatePersistencePort;

    @Mock
    private OtherEvidencePersistencePort otherEvidencePersistencePort;

    @Mock
    private CategoryPersistencePort categoryPersistencePort;

    @Mock
    private ScorePersistencePort scorePersistencePort;

    @Mock
    private S3Port s3Port;

    @Mock
    private CurrentMemberProvider currentMemberProvider;

    @InjectMocks
    private CreateCertificateService createCertificateService;

    @Nested
    @DisplayName("execute(name, date, file) 메서드는")
    class Describe_execute {

        @Test
        @DisplayName("자격증을 등록하고 점수를 저장한다")
        void it_creates_certificate_and_updates_score() throws IOException {
            // given
            String certificateName = "정보처리기사";
            LocalDate acquisitionDate = LocalDate.of(2024, 5, 1);
            MockMultipartFile file = new MockMultipartFile(
                    "file", "cert.pdf", "application/pdf", "dummy".getBytes()
            );
            Member member = Member.builder().email("test@gsm.hs.kr").build();
            Category category = Category.builder()
                    .name("MAJOR-CERTIFICATE-NUM")
                    .maximumValue(6)
                    .build();
            Score score = Score.builder()
                    .value(2)
                    .category(category)
                    .member(member)
                    .build();
            when(currentMemberProvider.getCurrentUser()).thenReturn(member);
            when(scorePersistencePort.findScoreByCategoryNameAndMemberEmailWithLock("MAJOR-CERTIFICATE-NUM", member.getEmail()))
                    .thenReturn(score);
            when(scorePersistencePort.saveScore(any(Score.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(s3Port.uploadFile(anyString(), any())).thenReturn(CompletableFuture.completedFuture("https://s3.com/cert.pdf"));
            when(otherEvidencePersistencePort.saveOtherEvidence(any(OtherEvidence.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // when
            createCertificateService.execute(certificateName, acquisitionDate, file);

            // then
            verify(scorePersistencePort).saveScore(any(Score.class));
            verify(certificatePersistencePort).saveCertificate(any());
            verify(otherEvidencePersistencePort).saveOtherEvidence(any(OtherEvidence.class));
        }
    }
}