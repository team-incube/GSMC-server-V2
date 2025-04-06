package team.incude.gsmc.v2.domain.certificate.application.usecase;

import team.incude.gsmc.v2.domain.certificate.persentation.data.response.GetCertificatesResponse;

public interface FindCertificateByEmailUseCase {
    GetCertificatesResponse execute(String email);
}