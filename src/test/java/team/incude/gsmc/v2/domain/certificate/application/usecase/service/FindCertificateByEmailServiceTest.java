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
import team.incude.gsmc.v2.domain.certificate.persentation.data.GetCertificateDto;
import team.incude.gsmc.v2.domain.certificate.persentation.data.response.GetCertificatesResponse;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("이메일을 이용해서 자격증을 찾는 서비스 클래스의")
class FindCertificateByEmailServiceTest {

    @Mock
    private CertificatePersistencePort certificatePersistencePort;

    @InjectMocks
    private FindCertificateByEmailService findCertificateByEmailService;

    @Nested
    @DisplayName("execute 메서드는")
    class Describe_execute {

        @Nested
        @DisplayName("유효한 이메일이 주어졌을 때")
        class Context_with_valid_email {

            @Test
            @DisplayName("자격증 목록을 반환한다.")
            void it_returns_certificate_list() {
                // given
                String email = "test@example.com";

                OtherEvidence otherEvidence1 = OtherEvidence.builder()
                        .fileUri("https://file1.com")
                        .build();

                OtherEvidence otherEvidence2 = OtherEvidence.builder()
                        .fileUri("https://file2.com")
                        .build();

                Certificate certificate1 = Certificate.builder()
                        .id(1L)
                        .name("정보처리기사")
                        .acquisitionDate(LocalDate.of(2022, 5, 20))
                        .evidence(otherEvidence1)
                        .build();

                Certificate certificate2 = Certificate.builder()
                        .id(2L)
                        .name("SQLD")
                        .acquisitionDate(LocalDate.of(2023, 6, 15))
                        .evidence(otherEvidence2)
                        .build();

                when(certificatePersistencePort.findCertificateByEmail(email))
                        .thenReturn(List.of(certificate1, certificate2));

                // when
                GetCertificatesResponse response = findCertificateByEmailService.execute(email);

                // then
                assertThat(response.certificates()).hasSize(2);
                assertThat(response.certificates()).extracting(GetCertificateDto::name)
                        .containsExactly("정보처리기사", "SQLD");
                assertThat(response.certificates()).extracting(GetCertificateDto::evidenceUri)
                        .containsExactly("https://file1.com", "https://file2.com");
            }
        }
    }
}