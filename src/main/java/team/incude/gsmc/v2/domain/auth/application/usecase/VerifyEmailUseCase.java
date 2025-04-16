package team.incude.gsmc.v2.domain.auth.application.usecase;

public interface VerifyEmailUseCase {
    void execute(String code);
}
