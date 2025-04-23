package team.incude.gsmc.v2.domain.certificate.persentation.data.request;

import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.annotations.NotNull;

import java.time.LocalDate;

public record CreateCertificateRequest(
        @NotNull String name,
        @NotNull LocalDate acquisitionDate,
        @NotNull MultipartFile file
) {
}