package team.incude.gsmc.v2.domain.certificate.application.port;

import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.certificate.persentation.data.response.GetCertificatesResponse;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.time.LocalDate;

@Port(direction = PortDirection.INBOUND)
public interface CertificateApplicationPort {
    GetCertificatesResponse findCurrentCertificate();

    GetCertificatesResponse findCertificateByStudentCode(String studentCode);

    void createCertificate(String name, LocalDate acquisitionDate, MultipartFile file);

    void updateCurrentCertificate(Long id, String name, LocalDate acquisitionDate, MultipartFile file);

    void deleteCurrentCertificate(Long id);

    void deleteCertificateByStudentCodeAndId(String studentCode, Long id);
}