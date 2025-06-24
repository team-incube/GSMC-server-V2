package team.incube.gsmc.v2.domain.certificate.application.usecase.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.incube.gsmc.v2.domain.certificate.application.port.CertificatePersistencePort;
import team.incube.gsmc.v2.domain.certificate.domain.Certificate;
import team.incube.gsmc.v2.domain.certificate.persentation.data.response.GetCertificateResponse;
import team.incube.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incube.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incube.gsmc.v2.domain.member.domain.Member;
import team.incube.gsmc.v2.domain.member.domain.StudentDetail;
import team.incube.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("자격증 정보 조회 서비스 클래스의")
class FindCertificateServiceTest {

    @Mock
    private CertificatePersistencePort certificatePersistencePort;

    @Mock
    private StudentDetailPersistencePort studentDetailPersistencePort;

    @Mock
    private CurrentMemberProvider currentMemberProvider;

    @InjectMocks
    private FindCertificateService findCertificateService;

    @Nested
    @DisplayName("execute() 메서드는")
    class Describe_execute_without_student_code {

        @Test
        @DisplayName("현재 로그인된 사용자의 자격증 목록을 반환한다")
        void it_returns_certificates_of_authenticated_user() {
            // given
            String email = "user@gsm.hs.kr";
            String studentCode = "24058";
            Member member = Member.builder().email(email).build();
            StudentDetail studentDetail = StudentDetail.builder()
                    .studentCode(studentCode)
                    .member(member)
                    .build();
            Certificate certificate = Certificate.builder()
                    .id(1L)
                    .name("정보처리기사")
                    .acquisitionDate(LocalDate.of(2023, 3, 15))
                    .evidence(OtherEvidence.builder().fileUri("https://evidence.com/1").build())
                    .build();
            when(currentMemberProvider.getCurrentUser()).thenReturn(member);
            when(studentDetailPersistencePort.findStudentDetailByMemberEmail(email)).thenReturn(studentDetail);
            when(certificatePersistencePort.findCertificateByStudentDetailStudentCode(studentCode)).thenReturn(List.of(certificate));

            // when
            GetCertificateResponse response = findCertificateService.execute();

            // then
            assertThat(response.certificates()).hasSize(1);
            assertThat(response.certificates().get(0).name()).isEqualTo("정보처리기사");
            assertThat(response.certificates().get(0).evidenceUri()).isEqualTo("https://evidence.com/1");
        }
    }

    @Nested
    @DisplayName("execute(String studentCode) 메서드는")
    class Describe_execute_with_student_code {

        @Test
        @DisplayName("지정된 studentCode의 자격증 목록을 반환한다")
        void it_returns_certificates_of_given_student_code() {
            // given
            String studentCode = "24058";
            Certificate certificate = Certificate.builder()
                    .id(2L)
                    .name("SQLD")
                    .acquisitionDate(LocalDate.of(2022, 6, 1))
                    .evidence(OtherEvidence.builder().fileUri("https://evidence.com/2").build())
                    .build();
            when(certificatePersistencePort.findCertificateByStudentDetailStudentCode(studentCode))
                    .thenReturn(List.of(certificate));

            // when
            GetCertificateResponse response = findCertificateService.execute(studentCode);

            // then
            assertThat(response.certificates()).hasSize(1);
            assertThat(response.certificates().get(0).name()).isEqualTo("SQLD");
            assertThat(response.certificates().get(0).evidenceUri()).isEqualTo("https://evidence.com/2");
        }
    }
}