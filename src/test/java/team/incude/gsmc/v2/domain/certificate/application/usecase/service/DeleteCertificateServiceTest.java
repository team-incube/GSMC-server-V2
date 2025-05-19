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
import team.incude.gsmc.v2.domain.certificate.exception.CertificateNotBelongToMemberException;
import team.incude.gsmc.v2.domain.evidence.application.port.EvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.domain.score.exception.InvalidScoreValueException;
import team.incude.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    @DisplayName("execute(String email, Long id) 메서드는")
    class Describe_execute_with_email_and_id {

        @Nested
        @DisplayName("로그인된 사용자가 자격증을 삭제할 때")
        class Context_with_logged_in_member {

            @Test
            @DisplayName("본인의 자격증이면 삭제하고 점수를 감소시킨다")
            void when_own_certificate_then_delete_and_decrease_score() {
                Long certId = 1L;
                String email = "test@gsm.hs.kr";
                Member member = Member.builder().id(10L).email(email).build();

                Certificate certificate = Certificate.builder()
                        .id(certId)
                        .member(member)
                        .name("정보처리기사")
                        .evidence(OtherEvidence.builder()
                                .fileUri("https://bucket.s3.amazonaws.com/test.pdf")
                                .id(Evidence.builder().id(99L).build())
                                .build())
                        .build();
                Score score = Score.builder().value(2).member(member).build();

                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(certificatePersistencePort.findCertificateByIdWithLock(certId)).thenReturn(certificate);
                when(scorePersistencePort.findScoreByCategoryNameAndMemberEmailWithLock("MAJOR-CERTIFICATE_NUM", email)).thenReturn(score);

                deleteCertificateService.execute(certId);

                verify(scorePersistencePort).saveScore(argThat(s -> s.getValue() == 1));
                verify(certificatePersistencePort).deleteCertificateById(certId);
                verify(otherEvidencePersistencePort).deleteOtherEvidenceById(99L);
                verify(evidencePersistencePort).deleteEvidenceById(99L);
                verify(s3Port).deleteFile("test.pdf");
            }

            @Test
            @DisplayName("자격증이 본인의 것이 아니면 예외를 던진다")
            void when_certificate_does_not_belong_to_member_then_throw() {
                Member me = Member.builder().id(1L).email("me@gsm.hs.kr").build();
                Member other = Member.builder().id(2L).email("other@gsm.hs.kr").build();

                Certificate certificate = Certificate.builder()
                        .id(1L)
                        .member(other)
                        .build();

                when(currentMemberProvider.getCurrentUser()).thenReturn(me);
                when(certificatePersistencePort.findCertificateByIdWithLock(1L)).thenReturn(certificate);

                assertThatThrownBy(() -> deleteCertificateService.execute(1L))
                        .isInstanceOf(CertificateNotBelongToMemberException.class);
            }

            @Test
            @DisplayName("점수가 0이면 예외를 던진다")
            void when_score_is_zero_then_throw() {
                Member member = Member.builder().id(10L).email("test@gsm.hs.kr").build();
                Certificate certificate = Certificate.builder()
                        .id(1L)
                        .member(member)
                        .name("정보처리기사")
                        .evidence(OtherEvidence.builder()
                                .fileUri("https://bucket.s3.amazonaws.com/test.pdf")
                                .id(Evidence.builder().id(99L).build())
                                .build())
                        .build();
                Score score = Score.builder().value(0).member(member).build();

                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(certificatePersistencePort.findCertificateByIdWithLock(1L)).thenReturn(certificate);
                when(scorePersistencePort.findScoreByCategoryNameAndMemberEmailWithLock("MAJOR-CERTIFICATE_NUM", member.getEmail()))
                        .thenReturn(score);

                assertThatThrownBy(() -> deleteCertificateService.execute(1L))
                        .isInstanceOf(InvalidScoreValueException.class);
            }

            @Nested
            @DisplayName("자격증 이름이")
            class Context_with_certificate_name {

                @Test
                @DisplayName("한국사 자격증이면 한국사 카테고리 점수를 감소시킨다")
                void when_korean_history_certificate_then_decrease_korean_history_score() {
                    Member member = Member.builder().id(1L).email("test@gsm.hs.kr").build();
                    Certificate certificate = Certificate.builder()
                            .id(1L)
                            .member(member)
                            .name("한국사 능력검정 시험")
                            .evidence(OtherEvidence.builder()
                                    .fileUri("https://bucket.s3.amazonaws.com/korean.pdf")
                                    .id(Evidence.builder().id(123L).build())
                                    .build())
                            .build();
                    Score score = Score.builder().value(2).member(member).build();

                    when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                    when(certificatePersistencePort.findCertificateByIdWithLock(1L)).thenReturn(certificate);
                    when(scorePersistencePort.findScoreByCategoryNameAndMemberEmailWithLock("HUMANITIES-CERTIFICATE-KOREAN_HISTORY", member.getEmail()))
                            .thenReturn(score);

                    deleteCertificateService.execute(1L);

                    verify(scorePersistencePort).saveScore(argThat(s -> s.getValue() == 1));
                }

                @Test
                @DisplayName("한자 자격증이면 한자 카테고리 점수를 감소시킨다")
                void when_chinese_certificate_then_decrease_chinese_score() {
                    Member member = Member.builder().id(1L).email("test@gsm.hs.kr").build();
                    Certificate certificate = Certificate.builder()
                            .id(1L)
                            .member(member)
                            .name("한자검정시험 4급")
                            .evidence(OtherEvidence.builder()
                                    .fileUri("https://bucket.s3.amazonaws.com/hanja.pdf")
                                    .id(Evidence.builder().id(456L).build())
                                    .build())
                            .build();
                    Score score = Score.builder().value(2).member(member).build();

                    when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                    when(certificatePersistencePort.findCertificateByIdWithLock(1L)).thenReturn(certificate);
                    when(scorePersistencePort.findScoreByCategoryNameAndMemberEmailWithLock("HUMANITIES-CERTIFICATE-CHINESE_CHARACTER", member.getEmail()))
                            .thenReturn(score);

                    deleteCertificateService.execute(1L);

                    verify(scorePersistencePort).saveScore(argThat(s -> s.getValue() == 1));
                }
            }
        }
    }
}