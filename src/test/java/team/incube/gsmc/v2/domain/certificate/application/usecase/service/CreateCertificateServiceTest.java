package team.incube.gsmc.v2.domain.certificate.application.usecase.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockMultipartFile;
import team.incube.gsmc.v2.domain.certificate.application.port.CertificatePersistencePort;
import team.incube.gsmc.v2.domain.certificate.domain.Certificate;
import team.incube.gsmc.v2.domain.certificate.exception.DuplicateCertificateException;
import team.incube.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incube.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incube.gsmc.v2.domain.member.domain.Member;
import team.incube.gsmc.v2.domain.member.domain.StudentDetail;
import team.incube.gsmc.v2.domain.score.application.port.CategoryPersistencePort;
import team.incube.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incube.gsmc.v2.domain.score.domain.Category;
import team.incube.gsmc.v2.domain.score.domain.Score;
import team.incube.gsmc.v2.domain.score.exception.ScoreLimitExceededException;
import team.incube.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;
import team.incube.gsmc.v2.global.thirdparty.aws.exception.S3UploadFailedException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("자격증 생성 서비스 클래스의")
class CreateCertificateServiceTest {

    @Mock
    private CertificatePersistencePort certificatePersistencePort;
    @Mock
    private CategoryPersistencePort categoryPersistencePort;
    @Mock
    private OtherEvidencePersistencePort otherEvidencePersistencePort;
    @Mock
    private StudentDetailPersistencePort studentDetailPersistencePort;
    @Mock
    private ScorePersistencePort scorePersistencePort;
    @Mock
    private S3Port s3Port;
    @Mock
    private CurrentMemberProvider currentMemberProvider;

    @Mock
    private ApplicationContext applicationContext;

    @InjectMocks
    private CreateCertificateService createCertificateService;

    @Nested
    @DisplayName("execute 메서드는")
    class Describe_execute {

        @Nested
        @DisplayName("자격증 이름이 일반적인 경우")
        class Context_with_major_certificate {

            @Test
            @DisplayName("전공 점수를 1 증가시키고 자격증을 저장한다")
            void it_increases_major_score_and_saves_certificate() throws IOException {
                // given
                String certificateName = "정보처리기사";
                LocalDate date = LocalDate.of(2024, 5, 1);
                MockMultipartFile file = new MockMultipartFile("file", "cert.pdf", "application/pdf", "dummy".getBytes());
                Member member = Member.builder().email("test@gsm.hs.kr").build();
                Category category = Category.builder().name("MAJOR-CERTIFICATE_NUM").maximumValue(6).build();
                Score score = Score.builder().value(2).category(category).member(member).build();

                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(scorePersistencePort.findScoreByCategoryNameAndMemberEmailWithLock("MAJOR-CERTIFICATE_NUM", member.getEmail())).thenReturn(score);
                when(scorePersistencePort.saveScore(any())).thenAnswer(invocation -> invocation.getArgument(0));
                when(s3Port.uploadFile(any(), any())).thenReturn(CompletableFuture.completedFuture("https://s3.com/cert.pdf"));
                when(otherEvidencePersistencePort.saveOtherEvidence(any())).thenAnswer(invocation -> invocation.getArgument(0));

                // when
                createCertificateService.execute(certificateName, date, file);

                // then
                verify(scorePersistencePort).saveScore(any());
                verify(certificatePersistencePort).saveCertificate(any());
                verify(otherEvidencePersistencePort).saveOtherEvidence(any());
            }
        }

        @Nested
        @DisplayName("자격증 이름이 '한국사 능력검정'으로 시작할 때")
        class Context_with_korean_history_certificate {

            @Test
            @DisplayName("한국사 점수를 1 증가시키고 자격증을 저장한다")
            void it_increases_korean_history_score_and_saves_certificate() throws IOException {
                // given
                String certificateName = "한국사 능력검정 1급";
                LocalDate date = LocalDate.of(2024, 5, 1);
                MockMultipartFile file = new MockMultipartFile("file", "cert.pdf", "application/pdf", "dummy".getBytes());
                Member member = Member.builder().email("test@gsm.hs.kr").build();
                Category category = Category.builder().name("HUMANITIES-CERTIFICATE-KOREAN_HISTORY").maximumValue(6).build();
                Score score = Score.builder().value(1).category(category).member(member).build();

                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(scorePersistencePort.findScoreByCategoryNameAndMemberEmailWithLock("HUMANITIES-CERTIFICATE-KOREAN_HISTORY", member.getEmail())).thenReturn(score);
                when(scorePersistencePort.saveScore(any())).thenAnswer(invocation -> invocation.getArgument(0));
                when(s3Port.uploadFile(any(), any())).thenReturn(CompletableFuture.completedFuture("https://s3.com/cert.pdf"));
                when(otherEvidencePersistencePort.saveOtherEvidence(any())).thenAnswer(invocation -> invocation.getArgument(0));
                // when
                createCertificateService.execute(certificateName, date, file);

                // then
                verify(scorePersistencePort).saveScore(any());
                verify(certificatePersistencePort).saveCertificate(any());
            }
        }

        @Nested
        @DisplayName("점수가 최대값을 초과할 경우")
        class Context_when_score_is_maximum {

            @Test
            @DisplayName("ScoreLimitExceededException 예외를 던진다")
            void it_throws_ScoreLimitExceededException() throws IOException {
                // given
                String certificateName = "정보처리기사";
                LocalDate date = LocalDate.now();
                MockMultipartFile file = new MockMultipartFile("file", "cert.pdf", "application/pdf", "dummy".getBytes());
                Member member = Member.builder().email("max@test.com").build();
                Category category = Category.builder().name("MAJOR-CERTIFICATE_NUM").maximumValue(2).build();
                Score score = Score.builder().value(2).category(category).member(member).build();
                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(scorePersistencePort.findScoreByCategoryNameAndMemberEmailWithLock("MAJOR-CERTIFICATE_NUM", member.getEmail())).thenReturn(score);

                // when & then
                assertThatThrownBy(() -> createCertificateService.execute(certificateName, date, file))
                        .isInstanceOf(ScoreLimitExceededException.class);
                verify(scorePersistencePort, never()).saveScore(any());
                verify(certificatePersistencePort, never()).saveCertificate(any());
            }
        }

        @Test
        @DisplayName("S3 파일 업로드가 실패하면 예외를 던진다")
        void it_throws_exception_when_s3_upload_fails() throws IOException {
            // given
            String certificateName = "정보처리기사";
            LocalDate acquisitionDate = LocalDate.of(2024, 5, 1);
            MockMultipartFile file = new MockMultipartFile(
                    "file", "cert.pdf", "application/pdf", "dummy".getBytes()
            );
            Member member = Member.builder().email("test@gsm.hs.kr").build();
            Category category = Category.builder()
                    .name("MAJOR-CERTIFICATE_NUM")
                    .maximumValue(6)
                    .build();
            Score score = Score.builder()
                    .value(2)
                    .category(category)
                    .member(member)
                    .build();
            when(currentMemberProvider.getCurrentUser()).thenReturn(member);
            when(scorePersistencePort.findScoreByCategoryNameAndMemberEmailWithLock("MAJOR-CERTIFICATE_NUM", member.getEmail()))
                    .thenReturn(score);
            when(s3Port.uploadFile(anyString(), any()))
                    .thenThrow(new S3UploadFailedException());

            // when & then
            assertThatThrownBy(() ->
                    createCertificateService.execute(certificateName, acquisitionDate, file)
            ).isInstanceOf(S3UploadFailedException.class);
        }
    }

    @Nested
    @DisplayName("중복된 한자/한국사 자격증이 등록된 경우")
    class Context_with_duplicate_certificate {

        @Test
        @DisplayName("이미 등록된 한국사 자격증이 있으면 DuplicateCertificateException을 던진다")
        void it_throws_when_duplicate_korean_history_exists() {
            // given
            String certificateName = "한국사 능력검정 1급";
            LocalDate date = LocalDate.of(2024, 5, 1);
            MockMultipartFile file = new MockMultipartFile("file", "cert.pdf", "application/pdf", "dummy".getBytes());
            Member member = Member.builder().id(1L).email("test@gsm.hs.kr").build();
            when(currentMemberProvider.getCurrentUser()).thenReturn(member);
            when(certificatePersistencePort.findCertificateByMemberIdWithLock(1L))
                    .thenReturn(List.of(
                            Certificate.builder().name("한국사 능력검정 2급").build()
                    ));

            // when & then
            assertThatThrownBy(() -> createCertificateService.execute(certificateName, date, file))
                    .isInstanceOf(DuplicateCertificateException.class);
            verify(certificatePersistencePort, never()).saveCertificate(any());
        }
    }

    @Nested
    @DisplayName("중복된 전공 자격증이 등록된 경우")
    class Context_with_duplicate_major_certificate {

        @Test
        @DisplayName("이미 등록된 전공 자격증이 있으면 DuplicateCertificateException을 던진다")
        void it_throws_when_duplicate_major_exists() {
            // given
            String certificateName = "정보처리기사";
            LocalDate date = LocalDate.of(2024, 5, 1);
            MockMultipartFile file = new MockMultipartFile("file", "cert.pdf", "application/pdf", "dummy".getBytes());
            Member member = Member.builder().id(1L).email("s24058@gsm.hs.kr").build();
            when(currentMemberProvider.getCurrentUser()).thenReturn(member);
            when(certificatePersistencePort.existsByMemberIdAndName(1L, certificateName))
                    .thenReturn(true);

            // when & then
            assertThatThrownBy(() -> createCertificateService.execute(certificateName, date, file))
                    .isInstanceOf(DuplicateCertificateException.class);
            verify(certificatePersistencePort, never()).saveCertificate(any());
        }
    }
}