package team.incude.gsmc.v2.domain.certificate.application.usecase;

public interface DeleteCertificateUseCase {
    void execute(Long id);

    void execute(String studentCode, Long id);
}