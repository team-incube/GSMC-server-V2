package team.incude.gsmc.v2.domain.certificate.application;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.certificate.application.port.CertificateApplicationPort;
import team.incude.gsmc.v2.domain.certificate.application.usecase.CreateCertificateUseCase;
import team.incude.gsmc.v2.domain.certificate.application.usecase.DeleteCurrentCertificateUseCase;
import team.incude.gsmc.v2.domain.certificate.application.usecase.FindCertificateByEmailUseCase;
import team.incude.gsmc.v2.domain.certificate.application.usecase.UpdateCurrentCertificateUseCase;
import team.incude.gsmc.v2.domain.certificate.persentation.data.response.GetCertificatesResponse;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.time.LocalDate;

@Adapter(direction = PortDirection.INBOUND)
@RequiredArgsConstructor
public class CertificateApplicationAdapter implements CertificateApplicationPort {

    private final FindCertificateByEmailUseCase findCertificateByEmailUseCase;
    private final CreateCertificateUseCase createCertificateUseCase;
    private final UpdateCurrentCertificateUseCase updateCurrentCertificateUseCase;
    private final DeleteCurrentCertificateUseCase deleteCurrentCertificateUseCase;

    @Override
    public GetCertificatesResponse findCurrentCertificate() {
        return null;
    }

    @Override
    public GetCertificatesResponse findCertificateByEmail(String email) {
        return findCertificateByEmailUseCase.execute(email);
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
        deleteCurrentCertificateUseCase.execute(id);
    }
}