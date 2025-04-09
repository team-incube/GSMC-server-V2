package team.incude.gsmc.v2.domain.auth.application.usecase;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import team.incude.gsmc.v2.domain.auth.application.port.AuthCodePersistencePort;
import team.incude.gsmc.v2.domain.auth.application.port.AuthenticationPersistencePort;
import team.incude.gsmc.v2.domain.auth.application.util.GenerationAuthCode;
import team.incude.gsmc.v2.domain.auth.domain.AuthCode;
import team.incude.gsmc.v2.domain.auth.domain.Authentication;
import team.incude.gsmc.v2.domain.auth.exception.EmailAuthAttemptExceededException;
import team.incude.gsmc.v2.global.annotation.usecase.UseCase;
import team.incude.gsmc.v2.global.thirdparty.email.EmailSendService;

@UseCase
@RequiredArgsConstructor
public class SendAuthenticationEmailUseCase {

    private final AuthCodePersistencePort authCodePersistencePort;
    private final AuthenticationPersistencePort authenticationPersistencePort;
    private final EmailSendService emailSendService;
    @Value("${email.ttl}")
    private long ttl;
    @Value("${email.attempt-limits}")
    private int attemptCountLimit;

    public void execute(String email) throws MessagingException {
        if (authenticationPersistencePort.existsAuthenticationByEmail(email)) {
            Authentication authentication = authenticationPersistencePort.findAuthenticationByEmail(email);
            if (authentication.getAttemptCount() >= attemptCountLimit) {
                throw new EmailAuthAttemptExceededException();
            }
            Authentication updatedAuthentication = Authentication.builder()
                    .email(authentication.getEmail())
                    .attemptCount(authentication.getAttemptCount() + 1)
                    .verified(authentication.getVerified())
                    .ttl(authentication.getTtl())
                    .build();
            authenticationPersistencePort.saveAuthentication(updatedAuthentication);
        } else {
            Authentication newAuthentication = Authentication.builder()
                    .email(email)
                    .attemptCount(1)
                    .verified(false)
                    .ttl(ttl)
                    .build();
        }
        AuthCode authCode = AuthCode.builder()
                .email(email)
                .authCode(GenerationAuthCode.generateAuthCode())
                .ttl(ttl)
                .build();
        authCodePersistencePort.saveAuthCode(authCode);
        emailSendService.sendEmail(email, authCode.getAuthCode());
    }
}
