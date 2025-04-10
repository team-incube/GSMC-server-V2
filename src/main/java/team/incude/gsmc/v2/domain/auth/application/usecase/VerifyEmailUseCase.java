package team.incude.gsmc.v2.domain.auth.application.usecase;

import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.auth.application.port.AuthCodePersistencePort;
import team.incude.gsmc.v2.domain.auth.application.port.AuthenticationPersistencePort;
import team.incude.gsmc.v2.domain.auth.domain.AuthCode;
import team.incude.gsmc.v2.domain.auth.domain.Authentication;
import team.incude.gsmc.v2.domain.auth.exception.VerificationInvalidException;
import team.incude.gsmc.v2.global.annotation.usecase.UseCase;

@UseCase
@RequiredArgsConstructor
public class VerifyEmailUseCase {

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
