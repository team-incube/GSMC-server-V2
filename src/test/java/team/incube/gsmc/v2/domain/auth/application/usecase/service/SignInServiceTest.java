package team.incube.gsmc.v2.domain.auth.application.usecase.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import team.incube.gsmc.v2.domain.auth.application.port.JwtPort;
import team.incube.gsmc.v2.domain.auth.exception.PasswordInvalidException;
import team.incube.gsmc.v2.domain.auth.presentation.data.response.AuthTokenResponse;
import team.incube.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incube.gsmc.v2.domain.member.domain.Member;
import team.incube.gsmc.v2.domain.member.domain.constant.MemberRole;
import team.incube.gsmc.v2.global.security.jwt.data.TokenDto;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
@DisplayName("로그인 클래스의")
class SignInServiceTest {

    @Mock
    private MemberPersistencePort memberPersistencePort;

    @Mock
    private JwtPort jwtPort;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private SignInService signInService;

    @Nested
    @DisplayName("execute(email, password) 메소드는")
    class Describe_execute {

        @Nested
        @DisplayName("비밀번호가 일치하면")
        class valid_password {

            @Test
            @DisplayName("AccessToken과 RefreshToken을 반환한다.")
            void it_returns_tokens() {

                // given
                String email = "test@gsm.hs.kr";
                String password = "test123";
                String encodedPassword = "encodedPassword";
                MemberRole role = MemberRole.ROLE_STUDENT;
                Member member = Member.builder()
                        .email(email)
                        .password(encodedPassword)
                        .role(role)
                        .build();
                TokenDto accessToken = new TokenDto("accessToken", LocalDateTime.now().plusDays(1));
                TokenDto refreshToken = new TokenDto("refreshToken", LocalDateTime.now().plusDays(7));
                when(memberPersistencePort.findMemberByEmail(email)).thenReturn(member);
                when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
                when(jwtPort.issueAccessToken(email, role)).thenReturn(accessToken);
                when(jwtPort.issueRefreshToken(email)).thenReturn(refreshToken);

                // when
                AuthTokenResponse response = signInService.execute(email, password);

                // then
                assertAll(
                        () -> assertEquals(accessToken.token(), response.accessToken()),
                        () -> assertEquals(refreshToken.token(), response.refreshToken()),
                        () -> assertEquals(accessToken.expiration(), response.accessTokenExpiresAt()),
                        () -> assertEquals(refreshToken.expiration(), response.refreshTokenExpiresAt()),
                        () -> assertEquals(role, response.role())
                );

            }
        }

        @Nested
        @DisplayName("비밀번호가 유효하지 않으면")
        class invalid_password {

            @Test
            @DisplayName("PasswordInvalidException을 던진다.")
            void it_throws_passwordInvalidException() {

                // given
                String email = "test@gsm.hs.kr";
                String password = "test123";
                String encodedPassword = "encodedPassword";
                MemberRole role = MemberRole.ROLE_STUDENT;
                Member member = Member.builder()
                        .email(email)
                        .password(encodedPassword)
                        .role(role)
                        .build();
                when(memberPersistencePort.findMemberByEmail(email)).thenReturn(member);
                when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

                // then
                assertThrows(PasswordInvalidException.class, () ->
                        signInService.execute(email, password)
                );
            }
        }
    }
}
