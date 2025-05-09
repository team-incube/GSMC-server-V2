package team.incude.gsmc.v2.global.thirdparty.email.usecase;

public interface EmailSendUseCase {
    void execute(String to, String authCode);
}
