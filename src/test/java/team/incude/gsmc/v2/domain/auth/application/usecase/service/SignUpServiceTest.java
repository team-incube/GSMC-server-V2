package team.incude.gsmc.v2.domain.auth.application.usecase.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import team.incude.gsmc.v2.domain.auth.domain.Authentication;
import team.incude.gsmc.v2.domain.auth.exception.MemberExistException;
import team.incude.gsmc.v2.domain.auth.exception.MemberForbiddenException;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SignUpServiceTest {

    @InjectMocks
    private SignUpService signUpService;

    @Mock
    private MemberPersistencePort memberPersistencePort;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private StudentDetailPersistencePort studentDetailPersistencePort;

    @Mock
    private Authentication authentication;

    public SignUpServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void it_signup_failed_email_not_verified() {

        String email = "test.gsm.hs.kr";

        when(memberPersistencePort.existsMemberByEmail(email)).thenReturn(false);
        when(authentication.getVerified()).thenReturn(false);

        assertThrows(MemberForbiddenException.class,
                () -> signUpService.execute("test", email, "password1234"));
    }

    @Test
    void it_signup_failed_email_already_exists() {

        String email = "test@gsm.hs.kr";

        when(memberPersistencePort.existsMemberByEmail(email)).thenReturn(true);

        assertThrows(MemberExistException.class,
                () -> signUpService.execute("test", email, "password1234"));
    }
}
