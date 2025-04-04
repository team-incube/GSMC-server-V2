package team.incude.gsmc.v2.domain.member.persistence.mapper;

import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.member.persistence.entity.MemberJpaEntity;
import team.incude.gsmc.v2.global.mapper.GenericMapper;

@Component
public class MemberMapper implements GenericMapper<MemberJpaEntity, Member> {

    @Override
    public MemberJpaEntity toEntity(Member member) {
        return MemberJpaEntity.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .password(member.getPassword())
                .role(member.getRole())
                .build();
    }

    @Override
    public Member toDomain(MemberJpaEntity memberJpaEntity) {
        return Member.builder()
                .id(memberJpaEntity.getId())
                .name(memberJpaEntity.getName())
                .email(memberJpaEntity.getEmail())
                .password(memberJpaEntity.getPassword())
                .role(memberJpaEntity.getRole())
                .build();
    }
}