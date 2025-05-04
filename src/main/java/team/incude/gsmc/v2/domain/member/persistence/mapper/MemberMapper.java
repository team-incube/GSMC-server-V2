package team.incude.gsmc.v2.domain.member.persistence.mapper;

import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.member.persistence.entity.MemberJpaEntity;
import team.incude.gsmc.v2.global.mapper.GenericMapper;

/**
 * 도메인 {@link Member} 객체와 JPA 엔티티 {@link MemberJpaEntity} 간의 매핑을 담당하는 클래스입니다.
 * <p>{@link GenericMapper}를 구현하여 도메인 계층과 영속성 계층 간의 의존성을 분리하고, 일관된 변환 로직을 제공합니다.
 * <ul>
 *   <li>{@code toEntity} - 도메인 객체를 엔티티로 변환</li>
 *   <li>{@code toDomain} - 엔티티를 도메인 객체로 변환</li>
 * </ul>
 * 이 매퍼는 주로 영속계층 어댑터에서 사용됩니다.
 * @author snowykte0426
 */
@Component
public class MemberMapper implements GenericMapper<MemberJpaEntity, Member> {

    /**
     * {@link Member} 객체를 {@link MemberJpaEntity}로 변환합니다.
     * @param member 변환할 도메인 객체
     * @return 변환된 JPA 엔티티
     */
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

    /**
     * {@link MemberJpaEntity}를 {@link Member} 객체로 변환합니다.
     * @param memberJpaEntity 변환할 JPA 엔티티
     * @return 변환된 도메인 객체
     */
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