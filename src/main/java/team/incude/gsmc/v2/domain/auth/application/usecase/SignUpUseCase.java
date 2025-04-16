package team.incude.gsmc.v2.domain.auth.application.usecase;

public interface SignUpUseCase {
    void execute(String name, String email, String password);
}
