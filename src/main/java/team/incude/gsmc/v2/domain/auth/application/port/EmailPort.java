package team.incude.gsmc.v2.domain.auth.application.port;

public interface EmailPort {
    void sendEmail(String to, String authCode);
}
