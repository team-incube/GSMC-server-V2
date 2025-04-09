package team.incude.gsmc.v2.domain.auth.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import team.incude.gsmc.v2.domain.auth.exception.PasswordInvalidException;
import team.incude.gsmc.v2.domain.auth.presentation.data.response.AuthTokenResponse;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.member.exception.MemberNotFoundException;
import team.incude.gsmc.v2.global.annotation.usecase.UseCaseWithReadOnlyTransaction;
import team.incude.gsmc.v2.global.security.jwt.dto.TokenDto;
import team.incude.gsmc.v2.global.security.jwt.service.JwtIssueService;

@UseCaseWithReadOnlyTransaction
@RequiredArgsConstructor
public class SignInUseCase {

    private final JwtIssueService jwtIssueService;
    private final MemberPersistencePort memberPersistencePort;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthTokenResponse execute(String email, String password) {
        Member member = memberPersistencePort.findMemberByEmail(email).orElseThrow(MemberNotFoundException::new);
        if (bCryptPasswordEncoder.matches(password, member.getPassword())) {
            TokenDto accessToken = jwtIssueService.issueAccessToken(member.getEmail(), member.getRole());
            TokenDto refreshToken = jwtIssueService.issueRefreshToken(member.getEmail());
            return new AuthTokenResponse(
                    accessToken.token(),
                    refreshToken.token(),
                    accessToken.expiration(),
                    refreshToken.expiration(),
                    member.getRole()
            );
        } else {
            throw new PasswordInvalidException();
        }
    }
}
