package team.incude.gsmc.v2.domain.auth.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incude.gsmc.v2.domain.auth.application.port.AuthenticationPersistencePort;
import team.incude.gsmc.v2.domain.auth.exception.MemberForbiddenException;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.auth.application.usecase.ChangePasswordUseCase;
import team.incude.gsmc.v2.domain.member.domain.Member;

@Service
@Transactional
@RequiredArgsConstructor
public class ChangePasswordService implements ChangePasswordUseCase {

    private final AuthenticationPersistencePort authenticationPersistencePort;
    private final MemberPersistencePort memberPersistencePort;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void execute(String email, String newPassword) {
        Member member = memberPersistencePort.findMemberByEmail(email);
        if (!authenticationPersistencePort.findAuthenticationByEmail(member.getEmail()).getVerified()) {
            throw new MemberForbiddenException();
        }

        memberPersistencePort.updateMemberPassword(member.getId(), bCryptPasswordEncoder.encode(newPassword));
    }
}
