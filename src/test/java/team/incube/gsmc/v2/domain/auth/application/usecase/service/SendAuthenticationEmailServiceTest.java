package team.incube.gsmc.v2.domain.auth.application.usecase.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import team.incube.gsmc.v2.domain.auth.application.port.AuthCodePersistencePort;
import team.incube.gsmc.v2.domain.auth.application.port.AuthenticationPersistencePort;
import team.incube.gsmc.v2.domain.auth.application.port.EmailPort;
import team.incube.gsmc.v2.domain.auth.domain.AuthCode;
import team.incube.gsmc.v2.domain.auth.domain.Authentication;
import team.incube.gsmc.v2.domain.auth.exception.EmailAuthAttemptExceededException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("인증코드 전송 클래스의 ")
class SendAuthenticationEmailServiceTest {

    @Mock
    private AuthenticationPersistencePort authenticationPersistencePort;

    @Mock
    private AuthCodePersistencePort authCodePersistencePort;

    @Mock
    private EmailPort emailPort;

    @InjectMocks
    private SendAuthenticationEmailService sendAuthenticationEmailService;

    private final String email = "test@gsm.hs.kr";
    private final long ttl = 300L;
    private final int attemptsLimits = 5;

    @BeforeEach
    void set() {
        ReflectionTestUtils.setField(sendAuthenticationEmailService, "ttl", ttl);
        ReflectionTestUtils.setField(sendAuthenticationEmailService, "attemptCountLimit", attemptsLimits);
    }

    @Nested
    @DisplayName("execute(email) 메소드는")
    class Decribe_execute {

        @Nested
        @DisplayName("최초로 인증을 요청할 때")
        class it_requests_with_first_auth_request {

            @Test
            @DisplayName("인증객체를 저장하고 인증코드를 전송한다")
            void it_saves_new_authentication() {

                // given
                when(authenticationPersistencePort.existsAuthenticationByEmail(email)).thenReturn(false);

                // when
                sendAuthenticationEmailService.execute(email);

                // then
                verify(authenticationPersistencePort).saveAuthentication(argThat(
                        auth -> auth.getEmail().equals(email) &&
                                auth.getAttemptCount() == 1 &&
                                Boolean.FALSE.equals(auth.getVerified()) &&
                                auth.getTtl().equals(ttl)
                ));
                verify(authCodePersistencePort).saveAuthCode(any(AuthCode.class));
                verify(emailPort).sendEmail(eq(email), anyString());
            }
        }

        @Nested
        @DisplayName("인증요청이 존재할 때")
        class it_requests_with_existing_auth_request {

            @Test
            @DisplayName("시도 횟수가 초과되지 않았다면 업데이트 후 이메일을 전송한다")
            void it_updates_authentication() {

                // given
                Authentication existingAuthentication = Authentication.builder()
                        .email(email)
                        .attemptCount(1)
                        .verified(false)
                        .ttl(ttl)
                        .build();

                when(authenticationPersistencePort.existsAuthenticationByEmail(email)).thenReturn(true);
                when(authenticationPersistencePort.findAuthenticationByEmail(email)).thenReturn(existingAuthentication);

                // when
                sendAuthenticationEmailService.execute(email);

                // then
                verify(authCodePersistencePort).saveAuthCode(any(AuthCode.class));
                verify(emailPort).sendEmail(eq(email), anyString());
            }

            @Test
            @DisplayName("시도횟수 초과되었다면 예외를 던진다")
            void it_throws_attempt_exceeds_limits_exception() {

                // given
                Authentication attemptExceeded = Authentication.builder()
                        .email(email)
                        .attemptCount(attemptsLimits)
                        .verified(false)
                        .ttl(ttl)
                        .build();

                when(authenticationPersistencePort.existsAuthenticationByEmail(email)).thenReturn(true);
                when(authenticationPersistencePort.findAuthenticationByEmail(email)).thenReturn(attemptExceeded);

                // when
                assertThrows(EmailAuthAttemptExceededException.class,
                        () -> sendAuthenticationEmailService.execute(email)
                );

                // then
                verify(authCodePersistencePort, never()).saveAuthCode(any());
                verify(emailPort, never()).sendEmail(any(), any());
            }
        }
    }
}
