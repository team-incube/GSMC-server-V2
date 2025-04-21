package team.incude.gsmc.v2.domain.certificate.application.usecase.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.incude.gsmc.v2.domain.certificate.application.port.CertificatePersistencePort;
import team.incude.gsmc.v2.domain.certificate.domain.Certificate;
import team.incude.gsmc.v2.domain.evidence.application.port.EvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.global.security.jwt.usecase.service.CurrentMemberProvider;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("자격증 정보 삭제 서비스 클래스의")
class DeleteCertificateServiceTest {

    @Mock
    private ScorePersistencePort scorePersistencePort;

    @Mock
    private CertificatePersistencePort certificatePersistencePort;

    @Mock
    private EvidencePersistencePort evidencePersistencePort;

    @Mock
    private OtherEvidencePersistencePort otherEvidencePersistencePort;

    @Mock
    private MemberPersistencePort memberPersistencePort;

    @Mock
    private S3Port s3Port;

    @Mock
    private CurrentMemberProvider currentMemberProvider;

    @InjectMocks
    private DeleteCertificateService deleteCertificateService;

    @Nested
    @DisplayName("execute(id) 메서드는")
    class Describe_execute {

        @Test
        @DisplayName("해당 자격증을 삭제하고 점수를 감소시킨다")
        void it_deletes_certificate_and_decrements_score() {
            // given
            Long certId = 1L;
            String email = "test@gsm.hs.kr";
            Member member = Member.builder()
                    .id(10L)
                    .email(email)
                    .build();
            Certificate certificate = Certificate.builder()
                    .id(certId)
                    .member(member)
                    .evidence(
                            OtherEvidence.builder()
                                    .fileUri("https://bucket.s3.amazonaws.com/test.pdf")
                                    .id(Evidence.builder().id(99L).build())
                                    .build()
                    )
                    .build();
            Score score = Score.builder()
                    .value(2)
                    .member(member)
                    .build();
            when(currentMemberProvider.getCurrentUser()).thenReturn(member);
            when(certificatePersistencePort.findCertificateByIdWithLock(certId)).thenReturn(certificate);
            when(scorePersistencePort.findScoreByCategoryNameAndMemberEmailWithLock("MAJOR-CERTIFICATE-NUM", email)).thenReturn(score);

            // when
            deleteCertificateService.execute(certId);

            // then
            verify(scorePersistencePort).saveScore(argThat(s -> s.getValue() == 1));
            verify(certificatePersistencePort).deleteCertificateById(certId);
            verify(otherEvidencePersistencePort).deleteOtherEvidenceById(99L);
            verify(evidencePersistencePort).deleteEvidenceById(99L);
            verify(s3Port).deleteFile("test.pdf"); // 파일 키만 추출된 버전
        }
    }
}