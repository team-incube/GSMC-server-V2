package team.incude.gsmc.v2.domain.certificate.persistence.projection;

import java.time.LocalDate;

public record CertificateProjection(
        Long id,
        String name,
        LocalDate acquisitionDate,
        String fileUri
) {
}