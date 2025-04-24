package team.incude.gsmc.v2.global.thirdparty.email.application.usecase;

public interface EmailSendUseCase {
    void sendEmail(String to, String authCode);
}
