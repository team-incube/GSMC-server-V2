package team.incude.gsmc.v2.domain.auth.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.auth.application.port.AuthCodePersistencePort;
import team.incude.gsmc.v2.domain.auth.application.port.AuthenticationPersistencePort;
import team.incude.gsmc.v2.domain.auth.application.usecase.VerifyEmailUseCase;
import team.incude.gsmc.v2.domain.auth.domain.AuthCode;
import team.incude.gsmc.v2.domain.auth.domain.Authentication;
import team.incude.gsmc.v2.domain.auth.exception.VerificationInvalidException;

@Service
@RequiredArgsConstructor
public class VerifyEmailService implements VerifyEmailUseCase {

    private final AuthCodePersistencePort authCodePersistencePort;
    private final AuthenticationPersistencePort authenticationPersistencePort;

    public void execute(String code) {
        if(!authCodePersistencePort.existsAuthCodeByCode(code)) {
            throw new VerificationInvalidException();
        }

        AuthCode authCode = authCodePersistencePort.findAuthCodeByCode(code);
        authCodePersistencePort.deleteAuthCodeByCode(code);
        Authentication authentication = authenticationPersistencePort.findAuthenticationByEmail(authCode.getEmail());
        Authentication updatedAuth = Authentication.builder()
                .email(authCode.getEmail())
                .attemptCount(authentication.getAttemptCount())
                .verified(true)
                .ttl(authentication.getTtl())
                .build();
        authenticationPersistencePort.saveAuthentication(updatedAuth);
    }
}
