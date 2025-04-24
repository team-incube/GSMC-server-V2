package team.incude.gsmc.v2.domain.auth.application.usecase.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.global.security.jwt.usecase.JwtIssueUseCase;

@ExtendWith(MockitoExtension.class)
@DisplayName("로그인 클래스의")
class SignInServiceTest {

    @Mock
    private MemberPersistencePort memberPersistencePort;

    @Mock
    private JwtIssueUseCase jwtIssueUseCase;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;



    @InjectMocks
    private SignInService signInService;
}
