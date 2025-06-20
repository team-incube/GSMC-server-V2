package team.incude.gsmc.v2.global.thirdparty.discord.service;

public interface SendEvidenceUploadFailureAlertUseCase {
    void sendEvidenceUploadFailureAlert(Long evidenceId, String fileName, String email, Throwable exception);
}
