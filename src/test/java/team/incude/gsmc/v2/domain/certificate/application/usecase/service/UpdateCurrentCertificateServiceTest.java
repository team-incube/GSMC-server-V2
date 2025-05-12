package team.incude.gsmc.v2.domain.certificate.application.usecase.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.certificate.application.port.CertificatePersistencePort;
import team.incude.gsmc.v2.domain.certificate.domain.Certificate;
import team.incude.gsmc.v2.domain.certificate.exception.CertificateCategoryMismatchException;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.global.security.jwt.usecase.service.CurrentMemberProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("현재 인증된 사용자의 자격증 정보 수정 서비스 클래스의")
class UpdateCurrentCertificateServiceTest {

    @Mock
    private CertificatePersistencePort certificatePersistencePort;
    @Mock
    private OtherEvidencePersistencePort otherEvidencePersistencePort;
    @Mock
    private MemberPersistencePort memberPersistencePort;
    @Mock
    private S3Port s3Port;
    @Mock
    private CurrentMemberProvider currentMemberProvider;

    @InjectMocks
    private UpdateCurrentCertificateService updateCertificateService;

    @Nested
    @DisplayName("execute 메서드는")
    class Describe_execute {

        @Test
        @DisplayName("파일이 주어진 경우 자격증 정보를 업데이트하고 기존 파일을 삭제 후 새 파일을 저장한다")
        void it_updates_certificate_with_new_file() throws Exception {
            // given
            Long certificateId = 1L;
            String newName = "정보처리기사(갱신)";
            LocalDate newDate = LocalDate.of(2024, 4, 1);
            String oldFileUri = "https://s3.com/oldFile.pdf";
            String newFileUri = "https://s3.com/newFile.pdf";
            MultipartFile file = new MockMultipartFile("file", "newFile.pdf", "application/pdf", "new".getBytes());
            Member member = Member.builder().id(1L).email("test@gsm.hs.kr").build();
            Score score = Score.builder().id(1L).build();
            Evidence evidenceId = Evidence.builder().id(1L).score(score).build();
            Evidence evidence = Evidence.builder()
                    .evidenceType(EvidenceType.CERTIFICATE)
                    .reviewStatus(ReviewStatus.PENDING)
                    .createdAt(LocalDateTime.of(2023, 1, 1, 0, 0))
                    .updatedAt(LocalDateTime.of(2023, 1, 1, 0, 0))
                    .score(score)
                    .build();
            OtherEvidence existingOtherEvidence = OtherEvidence.builder()
                    .id(evidenceId)
                    .fileUri(oldFileUri)
                    .build();
            Certificate existingCertificate = Certificate.builder()
                    .id(certificateId)
                    .member(member)
                    .name("정보처리기사")
                    .acquisitionDate(LocalDate.of(2023, 3, 1))
                    .evidence(existingOtherEvidence)
                    .build();
            OtherEvidence newOtherEvidence = OtherEvidence.builder()
                    .id(evidence)
                    .fileUri(newFileUri)
                    .build();
            when(currentMemberProvider.getCurrentUser()).thenReturn(member);
            when(memberPersistencePort.findMemberByEmail(member.getEmail())).thenReturn(member);
            when(certificatePersistencePort.findCertificateByIdWithLock(certificateId)).thenReturn(existingCertificate);
            when(s3Port.uploadFile(eq("newFile.pdf"), any())).thenReturn(java.util.concurrent.CompletableFuture.completedFuture(newFileUri));
            when(otherEvidencePersistencePort.saveOtherEvidence(any(OtherEvidence.class))).thenReturn(newOtherEvidence);

            // when
            updateCertificateService.execute(certificateId, newName, newDate, file);

            // then
            verify(s3Port).deleteFile("oldFile.pdf");
            verify(certificatePersistencePort).saveCertificate(argThat(cert ->
                    cert.getName().equals(newName) &&
                            cert.getAcquisitionDate().equals(newDate) &&
                            cert.getEvidence().getFileUri().equals(newFileUri)
            ));
        }

        @Test
        @DisplayName("일반 자격증을 한자/한국사로 변경 시 예외가 발생한다")
        void it_throws_when_general_certificate_changes_to_specialized() {
            // given
            Long certificateId = 1L;
            String newName = "한국사 능력검정(2급)";
            LocalDate newDate = LocalDate.of(2024, 4, 1);
            Member member = Member.builder().id(1L).email("test@gsm.hs.kr").build();
            Score score = Score.builder().id(1L).build();
            Evidence evidence = Evidence.builder().id(1L).score(score).build();
            OtherEvidence existingOtherEvidence = OtherEvidence.builder()
                    .id(evidence)
                    .fileUri("https://s3.com/original.pdf")
                    .build();
            Certificate existingCertificate = Certificate.builder()
                    .id(certificateId)
                    .member(member)
                    .name("정보처리기사")
                    .acquisitionDate(LocalDate.of(2023, 3, 1))
                    .evidence(existingOtherEvidence)
                    .build();
            when(currentMemberProvider.getCurrentUser()).thenReturn(member);
            when(memberPersistencePort.findMemberByEmail(member.getEmail())).thenReturn(member);
            when(certificatePersistencePort.findCertificateByIdWithLock(certificateId)).thenReturn(existingCertificate);

            // when & then
            assertThrows(CertificateCategoryMismatchException.class, () ->
                    updateCertificateService.execute(certificateId, newName, newDate, null));
        }
    }
}