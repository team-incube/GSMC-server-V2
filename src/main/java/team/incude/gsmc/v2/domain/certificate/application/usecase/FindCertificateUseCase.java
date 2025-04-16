package team.incude.gsmc.v2.domain.certificate.application.usecase;

import team.incude.gsmc.v2.domain.certificate.persentation.data.response.GetCertificatesResponse;

public interface FindCertificateUseCase {
    GetCertificatesResponse execute();

    GetCertificatesResponse execute(String email);
}