package team.incude.gsmc.v2.global.security.jwt.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.member.exception.MemberNotFoundException;
import team.incude.gsmc.v2.domain.member.persistence.mapper.MemberMapper;
import team.incude.gsmc.v2.domain.member.persistence.repository.MemberJpaRepository;

@Service
@Getter
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberJpaRepository memberJpaRepository;
    private final MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Member member = memberJpaRepository.findById(Long.valueOf(userId))
                .map(memberMapper::toDomain)
                .orElseThrow(MemberNotFoundException::new);

        return new CustomUserDetails(member);
    }
}
