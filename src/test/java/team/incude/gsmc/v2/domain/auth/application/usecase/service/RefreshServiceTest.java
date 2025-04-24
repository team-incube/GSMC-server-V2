package team.incude.gsmc.v2.domain.auth.application.usecase.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.incude.gsmc.v2.domain.auth.exception.RefreshTokenInvalidException;
import team.incude.gsmc.v2.domain.auth.presentation.data.response.AuthTokenResponse;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.member.domain.constant.MemberRole;
import team.incude.gsmc.v2.global.security.jwt.dto.TokenDto;
import team.incude.gsmc.v2.global.security.jwt.usecase.service.JwtIssueService;
import team.incude.gsmc.v2.global.security.jwt.usecase.service.JwtParserService;
import team.incude.gsmc.v2.global.security.jwt.usecase.service.JwtRefreshManagementService;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JWT 토큰 재발급 클래스의")
class RefreshServiceTest {

    @Mock
    private JwtIssueService jwtIssueService;

    @Mock
    private JwtParserService jwtParserService;

    @Mock
    private JwtRefreshManagementService jwtRefreshManagementService;

    @Mock
    private MemberPersistencePort memberPersistencePort;

    @InjectMocks
    private RefreshService refreshService;

    @Nested
    @DisplayName("execute(refreshToken) 메서드는")
    class Describe_execute {


        @Nested
        @DisplayName("유효한 리프레시 토큰일 때")
        class valid_token {

            @Test
            @DisplayName("새 액세스토큰과 리프레시토큰을 반환한다.")
            void it_issued_new_access_token_and_refresh_token() {

                // given
                String refreshToken = "validrefreshtoken";
                String email = "test@gsm.hs.kr";
                MemberRole role = MemberRole.ROLE_STUDENT;
                Member member = Member.builder()
                        .email(email)
                        .role(role)
                        .build();
                LocalDateTime accessTokenExpiration = LocalDateTime.now().plusHours(1);
                LocalDateTime refreshTokenExpiration = LocalDateTime.now().plusDays(7);
                TokenDto newAccessToken = new TokenDto("new_accessToken", accessTokenExpiration);
                TokenDto newRefreshToken = new TokenDto("new_refreshToken", refreshTokenExpiration);

                when(jwtParserService.validateRefreshToken(refreshToken)).thenReturn(true);
                when(jwtParserService.getEmailFromRefreshToken(refreshToken)).thenReturn(email);
                when(memberPersistencePort.findMemberByEmail(email)).thenReturn(member);
                when(jwtIssueService.issueAccessToken(email, role)).thenReturn(newAccessToken);
                when(jwtIssueService.issueRefreshToken(refreshToken)).thenReturn(newRefreshToken);

                // when
                AuthTokenResponse response = refreshService.execute(refreshToken);

                // then
                assertAll(
                        () -> assertEquals("newAccessToken", response.accessToken()),
                        () -> assertEquals("newRefreshToken", response.refreshToken()),
                        () -> assertEquals(accessTokenExpiration, response.accessTokenExpiresAt()),
                        () -> assertEquals(refreshTokenExpiration, response.refreshTokenExpiresAt()),
                        () -> assertEquals(role, response.role())
                );

                verify(jwtRefreshManagementService).deleteRefreshToken(refreshToken);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 리프레시 토큰일때")
        class invalid_token {

            @Test
            @DisplayName("RefreshTokenInvalidException 예외를 던진다")
            void it_throws_refresh_token_invalid_exception() {

                // given
                String invalidToken = "invalidToken";
                when(jwtParserService.validateRefreshToken(invalidToken)).thenReturn(false);

                // when
                assertThrows(RefreshTokenInvalidException.class,
                        () -> refreshService.execute(invalidToken)
                );

                // then
                verify(jwtRefreshManagementService, never()).deleteRefreshToken(any());
            }
        }
    }
}
