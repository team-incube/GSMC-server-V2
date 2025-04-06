package team.incude.gsmc.v2.domain.certificate.persentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.incude.gsmc.v2.domain.certificate.application.port.CertificateApplicationPort;
import team.incude.gsmc.v2.domain.certificate.persentation.data.response.GetCertificatesResponse;

@RestController
@RequestMapping("/api/v2/certificates")
@RequiredArgsConstructor
public class CertificateWebAdapter {

    private final CertificateApplicationPort certificateApplicationPort;

    @GetMapping("/{email}")
    public ResponseEntity<GetCertificatesResponse> getCertificates(@PathVariable String email) {
        return ResponseEntity.status(HttpStatus.OK).body(certificateApplicationPort.findCertificateByEmail(email));
    }
}