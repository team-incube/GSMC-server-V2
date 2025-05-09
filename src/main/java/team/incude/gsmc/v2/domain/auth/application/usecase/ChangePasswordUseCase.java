package team.incude.gsmc.v2.domain.auth.application.usecase;

public interface ChangePasswordUseCase {
    void execute(String email, String newPassword);
}
