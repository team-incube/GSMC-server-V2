package team.incude.gsmc.v2.domain.certificate.application.port;

import team.incude.gsmc.v2.domain.certificate.domain.Certificate;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.util.List;

@Port(direction = PortDirection.OUTBOUND)
public interface CertificatePersistencePort {
    List<Certificate> findCertificateByEmail(String email);
}