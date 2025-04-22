package team.incude.gsmc.v2.domain.auth.application.usecase.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import team.incude.gsmc.v2.domain.auth.exception.PasswordInvalidException;
import team.incude.gsmc.v2.domain.auth.presentation.data.response.AuthTokenResponse;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.member.domain.constant.MemberRole;
import team.incude.gsmc.v2.global.security.jwt.dto.TokenDto;
import team.incude.gsmc.v2.global.security.jwt.usecase.JwtIssueUseCase;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SignInServiceTest {

    @InjectMocks
    private SignInService signInService;

    @Mock
    private JwtIssueUseCase jwtIssueUseCase;

    @Mock
    private MemberPersistencePort memberPersistencePort;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public SignInServiceTest() { MockitoAnnotations.openMocks(this); }

    @Test
    void it_signin_successfully() {
        String email = "test@gsm.hs.kr";
        String password = "test1234";

        Member member = Member.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .role(MemberRole.ROLE_STUDENT)
                .build();

        when(memberPersistencePort.findMemberByEmail(email)).thenReturn(member);
        when(bCryptPasswordEncoder.matches(password, member.getPassword())).thenReturn(true);
        when(jwtIssueUseCase.issueAccessToken(email, member.getRole())).thenReturn(new TokenDto("accessToken", LocalDateTime.now().plusHours(1L)));
        when(jwtIssueUseCase.issueRefreshToken(email)).thenReturn(new TokenDto("refreshToken", LocalDateTime.now().plusDays(7L)));

        AuthTokenResponse authTokenResponse = signInService.execute(email, password);

        assertNotNull(authTokenResponse);
        assertEquals("accessToken", authTokenResponse.accessToken());
        assertEquals("refreshToken", authTokenResponse.refreshToken());
    }

    @Test
    void it_signin_failed() {
        String email = "test@gsm.hs.kr";
        String password = "password1234";

        Member member = Member.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .role(MemberRole.ROLE_STUDENT)
                .build();

        when(memberPersistencePort.findMemberByEmail(email)).thenReturn(member);
        when(bCryptPasswordEncoder.matches(password, member.getPassword())).thenReturn(false);

        assertThrows(PasswordInvalidException.class,
                () -> signInService.execute(email, password));
    }
}