package team.incude.gsmc.v2.domain.member.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.member.exception.MemberNotFoundException;
import team.incude.gsmc.v2.domain.member.persistence.mapper.MemberMapper;
import team.incude.gsmc.v2.domain.member.persistence.repository.MemberJpaRepository;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.Optional;

import static team.incude.gsmc.v2.domain.member.persistence.entity.QMemberJpaEntity.memberJpaEntity;
import static team.incude.gsmc.v2.domain.member.persistence.entity.QStudentDetailJpaEntity.studentDetailJpaEntity;

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements MemberPersistencePort {

    private final MemberJpaRepository memberJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final MemberMapper memberMapper;

    @Override
    public Member findMemberByEmail(String email) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(memberJpaEntity)
                        .where(memberJpaEntity.email.eq(email))
                        .fetchOne()
        ).map(memberMapper::toDomain).orElseThrow(MemberNotFoundException::new);
    }

    @Override
    public Member findMemberByStudentDetailStudentCode(String studentCode) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(studentDetailJpaEntity.member)
                        .from(studentDetailJpaEntity)
                        .where(studentDetailJpaEntity.studentCode.eq(studentCode))
                        .fetchOne()
        ).map(memberMapper::toDomain).orElseThrow(MemberNotFoundException::new);
    }

    @Override
    public Boolean existsMemberByEmail(String email) {
        var result = jpaQueryFactory
                .selectOne()
                .from(memberJpaEntity)
                .where(memberJpaEntity.email.eq(email))
                .fetchFirst();
        return result != null;
    }

    @Override
    public Member saveMember(Member member) {
        return memberMapper.toDomain(memberJpaRepository.save(memberMapper.toEntity(member)));
    }

    @Override
    public Member findMemberById(Long memberId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(memberJpaEntity)
                        .where(memberJpaEntity.id.eq(memberId))
                        .fetchOne()
        ).map(memberMapper::toDomain).orElseThrow(MemberNotFoundException::new);
    }
}