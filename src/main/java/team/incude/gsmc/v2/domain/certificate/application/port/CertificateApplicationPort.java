package team.incude.gsmc.v2.domain.certificate.application.port;

import team.incude.gsmc.v2.domain.certificate.persentation.data.response.GetCertificatesResponse;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

@Port(direction = PortDirection.INBOUND)
public interface CertificateApplicationPort {
    GetCertificatesResponse findCertificateByEmail(String email);
}