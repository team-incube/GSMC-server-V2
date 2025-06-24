package team.incube.gsmc.v2.domain.auth.application.usecase.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import team.incube.gsmc.v2.domain.auth.application.port.AuthenticationPersistencePort;
import team.incube.gsmc.v2.domain.auth.domain.Authentication;
import team.incube.gsmc.v2.domain.auth.exception.EmailFormatInvalidException;
import team.incube.gsmc.v2.domain.auth.exception.MemberExistException;
import team.incube.gsmc.v2.domain.auth.exception.MemberForbiddenException;
import team.incube.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incube.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incube.gsmc.v2.domain.member.domain.StudentDetail;
import team.incube.gsmc.v2.domain.member.domain.constant.MemberRole;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("회원가입 클래스의")
class SignUpServiceTest {

    @Mock
    private AuthenticationPersistencePort authenticationPersistencePort;

    @Mock
    private StudentDetailPersistencePort studentDetailPersistencePort;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private MemberPersistencePort memberPersistencePort;

    @InjectMocks
    private SignUpService signUpService;

    @Nested
    @DisplayName("execute(name, email, password) 메소드는")
    class Describe_execute {

        @Nested
        @DisplayName("유효한 회원가입 정보가 주어졌을 때")
        class signs_up_with_valid_info {

            @Test
            @DisplayName("회원가입을 성공적으로 수행한다.")
            void it_signs_up_successfully() {

                // given
                String name = "홍길동";
                String email = "s24072@gsm.hs.kr";
                String password = "test123";
                String encodedPassword = "encodedPassword";
                String studentCode = "24072";

                when(authenticationPersistencePort.findAuthenticationByEmail(email))
                        .thenReturn(Authentication.builder().email(email).verified(true).build());
                when(studentDetailPersistencePort.findStudentDetailByStudentCodeWithLock(studentCode))
                        .thenReturn(StudentDetail.builder().studentCode(studentCode).build());
                when(memberPersistencePort.existsMemberByEmail(email)).thenReturn(false);
                when(bCryptPasswordEncoder.encode(password)).thenReturn(encodedPassword);

                // when
                signUpService.execute(name, email, password);

                // then
                verify(memberPersistencePort).saveMember(argThat(member ->
                        member.getEmail().equals(email)
                            && member.getPassword().equals(encodedPassword)
                            && member.getName().equals(name)
                            && member.getRole().equals(MemberRole.ROLE_STUDENT)
                ));
            }
        }

        @Nested
        @DisplayName("이메일 인증이 안 된 경우")
        class signs_up_with_unverified_authentication {

            @Test
            @DisplayName("MemberForbiddenException 을 던진다.")
            void it_throws_member_forbiddenException() {

                // given
                String email = "s24072@gsm.hs.kr";

                when(authenticationPersistencePort.findAuthenticationByEmail(email))
                        .thenReturn(Authentication.builder().email(email).verified(false).build());

                // then
                assertThrows(MemberForbiddenException.class, () ->
                        signUpService.execute("김철수", email, "test123")
                );
            }
        }

        @Nested
        @DisplayName("가입된 이메일인 경우")
        class signs_up_with_existing_email {

            @Test
            @DisplayName("memberExistsException 을 던진다.")
            void it_throws_member_exists_exception() {

                // given
                String email = "s24072@gsm.hs.kr";
                when(authenticationPersistencePort.findAuthenticationByEmail(email))
                        .thenReturn(Authentication.builder().email(email).verified(true).build());
                when(memberPersistencePort.existsMemberByEmail(email)).thenReturn(true);

                // then
                assertThrows(MemberExistException.class, () ->
                        signUpService.execute("김철수", email, "test123")
                );
            }
        }

        @Nested
        @DisplayName("이메일 형식이 올바르지 않은 경우")
        class signs_up_with_invalid_email_format {

            @Test
            @DisplayName("emailFormatInvalidException 을 던진다.")
            void it_throws_email_format_invalid_exception() {

                // given
                String email = "test@gmail.com";
                when(authenticationPersistencePort.findAuthenticationByEmail(email))
                        .thenReturn(Authentication.builder().email(email).verified(true).build());
                when(memberPersistencePort.existsMemberByEmail(email)).thenReturn(false);

                // then
                assertThrows(EmailFormatInvalidException.class, () ->
                        signUpService.execute("김철수", email, "test123")
                );
            }
        }
    }
}
