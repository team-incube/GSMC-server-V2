package team.incude.gsmc.v2.domain.auth.application.usecase.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.auth.application.port.AuthCodePersistencePort;
import team.incude.gsmc.v2.domain.auth.application.port.AuthenticationPersistencePort;
import team.incude.gsmc.v2.domain.auth.application.port.EmailPort;
import team.incude.gsmc.v2.domain.auth.application.usecase.SendAuthenticationEmailUseCase;
import team.incude.gsmc.v2.global.util.AuthCodeGenerator;
import team.incude.gsmc.v2.domain.auth.domain.AuthCode;
import team.incude.gsmc.v2.domain.auth.domain.Authentication;
import team.incude.gsmc.v2.domain.auth.exception.EmailAuthAttemptExceededException;

/**
 * 인증 이메일 전송 유스케이스의 구현 클래스입니다.
 * <p>{@link SendAuthenticationEmailUseCase}를 구현하며, 사용자 이메일에 인증 코드를 생성하고 전송합니다.
 * 이메일 인증 시도 횟수를 제한하고, TTL 기반 유효 기간을 적용하여 인증 상태를 관리합니다.
 * <p>기존 인증 기록이 존재하면 시도 횟수를 증가시키고, 초과 시 {@link EmailAuthAttemptExceededException}을 발생시킵니다.
 * 새로운 인증 기록이 없을 경우 초기 시도 기록과 함께 저장합니다.
 * 이메일은 {@link EmailPort}를 통해 실제 발송되며, 인증 코드는 {@link AuthCodeGenerator}에서 생성됩니다.
 * @author jihoonwjj
 */
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

    /**
     * 사용자 이메일로 인증 코드를 생성하고 전송합니다.
     * <p>이미 인증 기록이 존재할 경우 시도 횟수를 검사하고 증가시킵니다.
     * 시도 횟수가 설정된 제한치를 초과하면 {@link EmailAuthAttemptExceededException}을 발생시킵니다.
     * 새 인증 요청인 경우 TTL 및 초기 시도 횟수와 함께 인증 객체를 생성합니다.
     * <p>인증 코드는 {@link AuthCodeGenerator}를 통해 생성되며,
     * {@link EmailPort}를 통해 해당 이메일로 전송됩니다.
     * @param email 인증 코드를 발송할 사용자 이메일
     * @throws EmailAuthAttemptExceededException 인증 시도 횟수가 제한을 초과한 경우
     */
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
                .authCode(AuthCodeGenerator.generateAuthCode())
                .ttl(ttl)
                .build();
        authCodePersistencePort.saveAuthCode(authCode);
        emailPort.sendEmail(email, authCode.getAuthCode());
    }
}
