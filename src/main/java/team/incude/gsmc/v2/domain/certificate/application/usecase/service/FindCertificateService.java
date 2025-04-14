package team.incude.gsmc.v2.domain.certificate.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incude.gsmc.v2.domain.certificate.application.port.CertificatePersistencePort;
import team.incude.gsmc.v2.domain.certificate.application.usecase.FindCertificateUseCase;
import team.incude.gsmc.v2.domain.certificate.domain.Certificate;
import team.incude.gsmc.v2.domain.certificate.persentation.data.GetCertificateDto;
import team.incude.gsmc.v2.domain.certificate.persentation.data.response.GetCertificatesResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindCertificateService implements FindCertificateService {

    private final CertificatePersistencePort certificatePersistencePort;

    // TODO: auth 구현 전 임시 코드
    private void setSecurityContext(String email) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, "");
        SecurityContextHolder.clearContext();
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // TODO: auth 구현 전 임시 코드
    private String getAuthenticatedEmail() {
        setSecurityContext("s24058@gsm.hs.kr");
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public GetCertificatesResponse execute() {
        String email = getAuthenticatedEmail();
        return findCertificate(email);
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