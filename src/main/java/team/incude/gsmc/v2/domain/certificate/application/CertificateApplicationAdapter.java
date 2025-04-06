package team.incude.gsmc.v2.domain.certificate.application;

import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.certificate.application.port.CertificateApplicationPort;
import team.incude.gsmc.v2.domain.certificate.application.usecase.FindCertificateByEmailUseCase;
import team.incude.gsmc.v2.domain.certificate.persentation.data.response.GetCertificatesResponse;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

@Adapter(direction = PortDirection.INBOUND)
@RequiredArgsConstructor
public class CertificateApplicationAdapter implements CertificateApplicationPort {

    private final FindCertificateByEmailUseCase findCertificateByEmailUseCase;

    @Override
    public GetCertificatesResponse findCertificateByEmail(String email) {
        return findCertificateByEmailUseCase.execute(email);
    }
}