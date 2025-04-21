package team.incude.gsmc.v2.domain.member.application.usecase;

public interface ChangePasswordUseCase {
    void execute(String email, String newPassword);
}
