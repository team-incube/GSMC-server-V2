package team.incude.gsmc.v2.domain.certificate.application.usecase;

public interface DeleteCertificateByEmailUseCase {
    void execute(String email, Long id);
}