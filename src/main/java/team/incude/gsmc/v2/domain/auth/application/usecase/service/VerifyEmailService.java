package team.incude.gsmc.v2.domain.auth.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.auth.application.port.AuthCodePersistencePort;
import team.incude.gsmc.v2.domain.auth.application.port.AuthenticationPersistencePort;
import team.incude.gsmc.v2.domain.auth.application.usecase.VerifyEmailUseCase;
import team.incude.gsmc.v2.domain.auth.domain.AuthCode;
import team.incude.gsmc.v2.domain.auth.domain.Authentication;
import team.incude.gsmc.v2.domain.auth.exception.VerificationInvalidException;

/**
 * 이메일 인증 코드 검증 유스케이스 구현 클래스입니다.
 * <p>{@link VerifyEmailUseCase}를 구현하며, 전달된 인증 코드가 유효한지 확인하고
 * 해당 이메일의 인증 상태를 true로 갱신합니다.
 * <p>인증 코드는 검증 후 즉시 삭제되며, 인증 상태는 {@link AuthenticationPersistencePort}를 통해 갱신됩니다.
 * 코드가 존재하지 않는 경우 {@link VerificationInvalidException}을 발생시킵니다.
 * @author jihoonwjj
 */
@Service
@RequiredArgsConstructor
public class VerifyEmailService implements VerifyEmailUseCase {

    private final AuthCodePersistencePort authCodePersistencePort;
    private final AuthenticationPersistencePort authenticationPersistencePort;

    /**
     * 인증 코드를 검증하고 해당 이메일의 인증 상태를 true로 갱신합니다.
     * <p>전달된 인증 코드가 존재하지 않을 경우 {@link VerificationInvalidException}을 발생시키며,
     * 검증에 성공하면 코드는 삭제되고 인증 정보는 갱신됩니다.
     * @param code 사용자가 입력한 인증 코드
     * @throws VerificationInvalidException 인증 코드가 존재하지 않는 경우
     */
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
