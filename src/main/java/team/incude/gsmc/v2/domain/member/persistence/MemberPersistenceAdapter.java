package team.incude.gsmc.v2.domain.member.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.member.persistence.mapper.MemberMapper;
import team.incude.gsmc.v2.domain.member.persistence.repository.MemberJpaRepository;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.Optional;

import static team.incude.gsmc.v2.domain.member.persistence.entity.QMemberJpaEntity.memberJpaEntity;

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements MemberPersistencePort {

    private final MemberJpaRepository memberJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final MemberMapper memberMapper;

    @Override
    public Optional<Member> findMemberByEmail(String email) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(memberJpaEntity)
                        .where(memberJpaEntity.email.eq(email))
                        .fetchOne()
        ).map(memberMapper::toDomain);
    }
}