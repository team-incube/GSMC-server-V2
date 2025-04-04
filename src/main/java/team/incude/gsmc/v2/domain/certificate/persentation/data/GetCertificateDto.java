package team.incude.gsmc.v2.domain.certificate.persentation.data;

public record GetCertificateDto(
        Long id,
        String name,
        String acquisitionDate,
        String evidenceUri
) {
}