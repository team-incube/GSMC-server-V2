package team.incude.gsmc.v2.domain.auth.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.auth.application.usecase.SignInUseCase;
import team.incude.gsmc.v2.domain.auth.exception.PasswordInvalidException;
import team.incude.gsmc.v2.domain.auth.presentation.data.response.AuthTokenResponse;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.member.exception.MemberNotFoundException;
import team.incude.gsmc.v2.global.security.jwt.dto.TokenDto;
import team.incude.gsmc.v2.global.security.jwt.service.JwtIssueService;

@Service
@RequiredArgsConstructor
public class SignInService implements SignInUseCase {

    private final JwtIssueService jwtIssueService;
    private final MemberPersistencePort memberPersistencePort;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthTokenResponse execute(String email, String password) {
        Member member = memberPersistencePort.findMemberByEmail(email);
        if (member == null) { throw new MemberNotFoundException(); }
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
