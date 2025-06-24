package team.incube.gsmc.v2.domain.certificate.persentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.incube.gsmc.v2.domain.certificate.persentation.data.request.CreateCertificateRequest;
import team.incube.gsmc.v2.domain.certificate.application.port.CertificateApplicationPort;
import team.incube.gsmc.v2.domain.certificate.persentation.data.request.PatchCertificateRequest;
import team.incube.gsmc.v2.domain.certificate.persentation.data.response.GetCertificateResponse;

/**
 * 자격증 관련 HTTP 요청을 처리하는 Web 어댑터 클래스입니다.
 * <p>RESTful API 엔드포인트를 통해 자격증의 생성, 조회, 수정, 삭제 기능을 제공합니다.
 * <ul>
 *   <li>{@code GET /api/v2/certificates/current} - 현재 로그인한 사용자 기준 자격증 목록 조회</li>
 *   <li>{@code GET /api/v2/certificates/{studentCode}} - 특정 학생의 자격증 목록 조회</li>
 *   <li>{@code POST /api/v2/certificates} - 자격증 등록</li>
 *   <li>{@code PATCH /api/v2/certificates/current/{certificateId}} - 현재 로그인한 사용자의 자격증 수정</li>
 *   <li>{@code DELETE /api/v2/certificates/{studentCode}/{certificateId}} - 특정 학생의 자격증 삭제</li>
 *   <li>{@code DELETE /api/v2/certificates/current/{certificateId}} - 현재 로그인한 사용자의 자격증 삭제</li>
 * </ul>
 * <p>{@link CertificateApplicationPort}를 통해 비즈니스 로직을 위임하며, 계층 간의 의존성을 분리합니다.
 * @author snowykte0426
 */
@RestController
@RequestMapping("/api/v2/certificates")
@RequiredArgsConstructor
public class CertificateWebAdapter {

    private final CertificateApplicationPort certificateApplicationPort;

    @GetMapping("/current")
    public ResponseEntity<GetCertificateResponse> getCurrentCertificates() {
        return ResponseEntity.status(HttpStatus.OK).body(certificateApplicationPort.findCurrentCertificate());
    }

    @GetMapping("/{studentCode}")
    public ResponseEntity<GetCertificateResponse> getCertificates(@PathVariable(value = "studentCode") String studentCode) {
        return ResponseEntity.status(HttpStatus.OK).body(certificateApplicationPort.findCertificateByStudentCode(studentCode));
    }

    @PostMapping
    public ResponseEntity<Void> createCertificates(@Valid @ModelAttribute CreateCertificateRequest request) {
        certificateApplicationPort.createCertificate(request.name(), request.acquisitionDate(), request.file());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/current/{certificateId}")
    public ResponseEntity<Void> updateCurrentCertificates(
            @PathVariable(value = "certificateId") Long certificateId,
            @Valid @ModelAttribute PatchCertificateRequest request
    ) {
        certificateApplicationPort.updateCurrentCertificate(certificateId, request.name(), request.acquisitionDate(), request.file());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{studentCode}/{certificateId}")
    public ResponseEntity<Void> deleteCertificates(
            @PathVariable(value = "studentCode") String studentCode,
            @PathVariable(value = "certificateId") Long certificateId
    ) {
        certificateApplicationPort.deleteCertificateByStudentCodeAndId(studentCode, certificateId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/current/{certificateId}")
    public ResponseEntity<Void> deleteCurrentCertificates(@PathVariable(value = "certificateId") Long certificateId) {
        certificateApplicationPort.deleteCurrentCertificate(certificateId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}