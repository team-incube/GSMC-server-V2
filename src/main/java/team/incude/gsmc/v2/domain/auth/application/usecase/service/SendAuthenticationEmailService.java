package team.incude.gsmc.v2.domain.auth.application.usecase.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.auth.application.port.AuthCodePersistencePort;
import team.incude.gsmc.v2.domain.auth.application.port.AuthenticationPersistencePort;
import team.incude.gsmc.v2.domain.auth.application.port.EmailPort;
import team.incude.gsmc.v2.domain.auth.application.usecase.SendAuthenticationEmailUseCase;
import team.incude.gsmc.v2.domain.auth.application.util.GenerationAuthCode;
import team.incude.gsmc.v2.domain.auth.domain.AuthCode;
import team.incude.gsmc.v2.domain.auth.domain.Authentication;
import team.incude.gsmc.v2.domain.auth.exception.EmailAuthAttemptExceededException;
import team.incude.gsmc.v2.global.thirdparty.email.usecase.EmailSendUseCase;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendAuthenticationEmailService implements SendAuthenticationEmailUseCase {

    private final AuthCodePersistencePort authCodePersistencePort;
    private final AuthenticationPersistencePort authenticationPersistencePort;
    private final EmailPort emailPort;
    @Value("${mail.ttl}")
    private long ttl;
    @Value("${mail.attempt-limits}")
    private int attemptCountLimit;

    public void execute(String email) {
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
            authenticationPersistencePort.saveAuthentication(newAuthentication);
        }
        AuthCode authCode = AuthCode.builder()
                .email(email)
                .authCode(GenerationAuthCode.generateAuthCode())
                .ttl(ttl)
                .build();
        authCodePersistencePort.saveAuthCode(authCode);
        emailPort.sendEmail(email, authCode.getAuthCode());
    }
}
