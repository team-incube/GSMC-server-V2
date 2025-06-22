package team.incube.gsmc.v2.domain.member.domain.constant;

import org.springframework.security.core.GrantedAuthority;

public enum MemberRole implements GrantedAuthority {
    ROLE_ADMIN,
    ROLE_TEACHER,
    ROLE_STUDENT;

    @Override
    public String getAuthority() {
        return name();
    }
}