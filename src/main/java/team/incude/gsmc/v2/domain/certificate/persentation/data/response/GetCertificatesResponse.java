package team.incude.gsmc.v2.domain.certificate.persentation.data.response;

import team.incude.gsmc.v2.domain.certificate.persentation.data.GetCertificateDto;

import java.util.List;

public record GetCertificatesResponse(
        List<GetCertificateDto> certificates
) {
}