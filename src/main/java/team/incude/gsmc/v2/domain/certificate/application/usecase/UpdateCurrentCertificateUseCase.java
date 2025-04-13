package team.incude.gsmc.v2.domain.certificate.application.usecase;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public interface UpdateCurrentCertificateUseCase {
    void execute(Long id, String name, LocalDate acquisitionDate, MultipartFile file);
}