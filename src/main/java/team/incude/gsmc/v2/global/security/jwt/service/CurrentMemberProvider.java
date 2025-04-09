package team.incude.gsmc.v2.global.security.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.member.exception.MemberNotFoundException;
import team.incude.gsmc.v2.domain.member.persistence.entity.MemberJpaEntity;
import team.incude.gsmc.v2.domain.member.persistence.repository.MemberJpaRepository;
import team.incude.gsmc.v2.global.security.exception.MemberUnauthorizedException;
import team.incude.gsmc.v2.global.security.jwt.auth.CustomUserDetails;

@Component
@RequiredArgsConstructor
public class CurrentMemberProvider {

    private final MemberJpaRepository memberJpaRepository;

    public MemberJpaEntity getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetails) {
            String userId = ((CustomUserDetails) principal).getUsername();
            return memberJpaRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new MemberNotFoundException());
        } else {
            throw new MemberUnauthorizedException();
        }
    }
}
