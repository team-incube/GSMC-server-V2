package team.incude.gsmc.v2.domain.auth.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.auth.application.usecase.SignInUseCase;
import team.incude.gsmc.v2.domain.auth.exception.PasswordInvalidException;
import team.incude.gsmc.v2.domain.auth.presentation.data.response.AuthTokenResponse;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.global.security.jwt.dto.TokenDto;
import team.incude.gsmc.v2.global.security.jwt.usecase.JwtIssueUseCase;

@Service
@RequiredArgsConstructor
public class SignInService implements SignInUseCase {

    private final JwtIssueUseCase jwtIssueUseCase;
    private final MemberPersistencePort memberPersistencePort;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthTokenResponse execute(String email, String password) {
        Member member = memberPersistencePort.findMemberByEmail(email);
        if (bCryptPasswordEncoder.matches(password, member.getPassword())) {
            TokenDto accessToken = jwtIssueUseCase.issueAccessToken(member.getEmail(), member.getRole());
            TokenDto refreshToken = jwtIssueUseCase.issueRefreshToken(member.getEmail());
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
