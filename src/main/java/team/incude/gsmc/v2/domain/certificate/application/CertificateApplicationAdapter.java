package team.incude.gsmc.v2.domain.certificate.application;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.certificate.application.port.CertificateApplicationPort;
import team.incude.gsmc.v2.domain.certificate.application.usecase.*;
import team.incude.gsmc.v2.domain.certificate.persentation.data.response.GetCertificatesResponse;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.time.LocalDate;

@Adapter(direction = PortDirection.INBOUND)
@RequiredArgsConstructor
public class CertificateApplicationAdapter implements CertificateApplicationPort {

    private final FindCertificateUseCase findCertificateUseCase;
    private final CreateCertificateUseCase createCertificateUseCase;
    private final UpdateCurrentCertificateUseCase updateCurrentCertificateUseCase;
    private final DeleteCertificateUseCase deleteCertificateUseCase;

    @Override
    public GetCertificatesResponse findCurrentCertificate() {
        return findCertificateUseCase.execute();
    }

    @Override
    public GetCertificatesResponse findCertificateByEmail(String email) {
        return findCertificateUseCase.execute(email);
    }

    @Override
    public void createCertificate(String name, LocalDate acquisitionDate, MultipartFile file) {
        createCertificateUseCase.execute(name, acquisitionDate, file);
    }

    @Override
    public void updateCurrentCertificate(Long id, String name, LocalDate acquisitionDate, MultipartFile file) {
        updateCurrentCertificateUseCase.execute(id, name, acquisitionDate, file);
    }

    @Override
    public void deleteCurrentCertificate(Long id) {
        deleteCertificateUseCase.execute(id);
    }

    @Override
    public void deleteCertificateByEmailAndId(String email, Long id) {
        deleteCertificateUseCase.execute(email, id);
    }
}