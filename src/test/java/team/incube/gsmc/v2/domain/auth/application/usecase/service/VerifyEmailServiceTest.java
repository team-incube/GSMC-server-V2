package team.incube.gsmc.v2.domain.auth.application.usecase.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.incube.gsmc.v2.domain.auth.application.port.AuthCodePersistencePort;
import team.incube.gsmc.v2.domain.auth.application.port.AuthenticationPersistencePort;
import team.incube.gsmc.v2.domain.auth.domain.AuthCode;
import team.incube.gsmc.v2.domain.auth.domain.Authentication;
import team.incube.gsmc.v2.domain.auth.exception.VerificationInvalidException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("인증이메일 확인 클래스의")
class VerifyEmailServiceTest {

    @Mock
    private AuthCodePersistencePort authCodePersistencePort;

    @Mock
    private AuthenticationPersistencePort authenticationPersistencePort;

    @InjectMocks
    private  VerifyEmailService verifyEmailService;

    @Nested
    @DisplayName("유효한 인증 코드 일 때")
    class valid_auth_code {

        @Test
        @DisplayName("인증된 인증정보로 업데이트한다.")
        void it_updates_authentication_verified() {

            // given
            String code = "123456";
            String email = "test@gmail.com";
            AuthCode authCode = AuthCode.builder()
                    .email(email)
                    .authCode(code)
                    .ttl(300L)
                    .build();
            Authentication authentication = Authentication.builder()
                    .email(email)
                    .attemptCount(1)
                    .verified(false)
                    .ttl(300L)
                    .build();

            when(authCodePersistencePort.existsAuthCodeByCode(code)).thenReturn(true);
            when(authCodePersistencePort.findAuthCodeByCode(code)).thenReturn(authCode);
            when(authenticationPersistencePort.findAuthenticationByEmail(email)).thenReturn(authentication);

            // when
            verifyEmailService.execute(code);

            // then
            verify(authCodePersistencePort).deleteAuthCodeByCode(code);
            verify(authenticationPersistencePort).saveAuthentication(argThat(auth ->
                        auth.getEmail().equals(email)
                    && auth.getAttemptCount() == 1
                    && auth.getTtl() == 300L
                    && Boolean.TRUE.equals(auth.getVerified())
            ));
        }
    }

    @Nested
    @DisplayName("유효하지 않은 인증코드 일 때")
    class invalid_auth_code {

        @Test
        @DisplayName("verificationInvalidException 을 던진다.")
        void it_throws_verification_invalid_exception() {

            // given
            String invalidCode = "invalidCode";

            when(authCodePersistencePort.existsAuthCodeByCode(invalidCode)).thenReturn(false);

            // then
            assertThrows(VerificationInvalidException.class, () ->
                    verifyEmailService.execute(invalidCode)
            );
            verify(authCodePersistencePort, never()).deleteAuthCodeByCode(any());
            verify(authenticationPersistencePort, never()).saveAuthentication(any());
        }
    }
}
