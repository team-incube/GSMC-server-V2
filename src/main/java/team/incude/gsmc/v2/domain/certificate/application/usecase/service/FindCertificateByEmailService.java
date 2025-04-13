package team.incude.gsmc.v2.domain.certificate.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incude.gsmc.v2.domain.certificate.application.port.CertificatePersistencePort;
import team.incude.gsmc.v2.domain.certificate.application.usecase.FindCertificateByEmailUseCase;
import team.incude.gsmc.v2.domain.certificate.persentation.data.GetCertificateDto;
import team.incude.gsmc.v2.domain.certificate.persentation.data.response.GetCertificatesResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindCertificateByEmailService implements FindCertificateByEmailUseCase {

    private final CertificatePersistencePort certificatePersistencePort;

    @Override
    public GetCertificatesResponse execute(String email) {
        return new GetCertificatesResponse(certificatePersistencePort.findCertificateByMemberEmail(email)
        .stream()
                .map(certificate -> new GetCertificateDto(
                        certificate.getId(),
                        certificate.getName(),
                        certificate.getAcquisitionDate().toString(),
                        certificate.getEvidence().getFileUri()
                )).toList());
    }
}