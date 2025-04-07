package team.incude.gsmc.v2.domain.certificate.persentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.certificate.application.port.CertificateApplicationPort;
import team.incude.gsmc.v2.domain.certificate.persentation.data.response.GetCertificatesResponse;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v2/certificates")
@RequiredArgsConstructor
public class CertificateWebAdapter {

    private final CertificateApplicationPort certificateApplicationPort;

    @GetMapping("/current")
    public ResponseEntity<GetCertificatesResponse> getCurrentCertificates() {
        return ResponseEntity.status(HttpStatus.OK).body(certificateApplicationPort.findCurrentCertificate());
    }

    @GetMapping("/{email}")
    public ResponseEntity<GetCertificatesResponse> getCertificates(@PathVariable String email) {
        return ResponseEntity.status(HttpStatus.OK).body(certificateApplicationPort.findCertificateByEmail(email));
    }

    @PostMapping
    public ResponseEntity<Void> createCertificates(
            @RequestParam("name") String name,
            @RequestParam("acquisitionDate") LocalDate acquisitionDate,
            @RequestParam("file") MultipartFile file
    ) {
        certificateApplicationPort.createCertificate(name, acquisitionDate, file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}