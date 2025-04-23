package team.incude.gsmc.v2.domain.certificate.persentation.data.request;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record PatchCertificateRequest(
        String name,
        LocalDate acquisitionDate,
        MultipartFile file
) {
}