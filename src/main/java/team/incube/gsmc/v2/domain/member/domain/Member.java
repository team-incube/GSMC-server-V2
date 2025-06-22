package team.incube.gsmc.v2.domain.member.domain;

import lombok.Builder;
import lombok.Getter;
import team.incube.gsmc.v2.domain.member.domain.constant.MemberRole;

@Getter
@Builder
public class Member {
    private Long id;
    private String name;
    private String email;
    private String password;
    private MemberRole role;
}