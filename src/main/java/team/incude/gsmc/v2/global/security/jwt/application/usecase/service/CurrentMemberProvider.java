package team.incude.gsmc.v2.global.security.jwt.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.global.security.exception.MemberUnauthorizedException;

@Component
@RequiredArgsConstructor
public class CurrentMemberProvider {

    private final MemberPersistencePort memberPersistencePort;

    public Member getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof String email) {
            return memberPersistencePort.findMemberByEmail(email);
        }

        throw new MemberUnauthorizedException();
    }
}