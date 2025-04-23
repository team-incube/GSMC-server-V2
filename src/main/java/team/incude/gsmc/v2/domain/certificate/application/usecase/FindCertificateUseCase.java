package team.incude.gsmc.v2.domain.certificate.application.usecase;

import team.incude.gsmc.v2.domain.certificate.persentation.data.response.GetCertificateResponse;

public interface FindCertificateUseCase {
    GetCertificateResponse execute();

    GetCertificateResponse execute(String studentCode);
}