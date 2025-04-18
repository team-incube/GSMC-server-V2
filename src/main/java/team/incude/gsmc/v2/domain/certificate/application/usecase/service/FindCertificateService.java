package team.incude.gsmc.v2.domain.certificate.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incude.gsmc.v2.domain.certificate.application.port.CertificatePersistencePort;
import team.incude.gsmc.v2.domain.certificate.application.usecase.FindCertificateUseCase;
import team.incude.gsmc.v2.domain.certificate.persentation.data.GetCertificateDto;
import team.incude.gsmc.v2.domain.certificate.persentation.data.response.GetCertificatesResponse;
import team.incude.gsmc.v2.global.security.jwt.usecase.service.CurrentMemberProvider;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindCertificateService implements FindCertificateUseCase {

    private final CertificatePersistencePort certificatePersistencePort;
    private final CurrentMemberProvider currentMemberProvider;

    @Override
    public GetCertificatesResponse execute() {
        return findCertificate(currentMemberProvider.getCurrentUser().getEmail());
    }

    @Override
    public GetCertificatesResponse execute(String email) {
        return findCertificate(email);
    }

    private GetCertificatesResponse findCertificate(String email) {
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