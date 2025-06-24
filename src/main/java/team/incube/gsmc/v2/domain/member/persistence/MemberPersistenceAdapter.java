package team.incube.gsmc.v2.domain.member.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.incube.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incube.gsmc.v2.domain.member.domain.Member;
import team.incube.gsmc.v2.domain.member.exception.MemberNotFoundException;
import team.incube.gsmc.v2.domain.member.persistence.mapper.MemberMapper;
import team.incube.gsmc.v2.domain.member.persistence.repository.MemberJpaRepository;
import team.incube.gsmc.v2.global.annotation.PortDirection;
import team.incube.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.Optional;

import static team.incube.gsmc.v2.domain.member.persistence.entity.QMemberJpaEntity.memberJpaEntity;

/**
 * 사용자(Member) 도메인의 영속성 어댑터 구현체입니다.
 * <p>{@link MemberPersistencePort}를 구현하며, JPA 및 QueryDSL을 통해 사용자 정보를 조회, 저장, 수정합니다.
 * <p>{@link MemberMapper}를 통해 도메인 객체와 엔티티 간 변환을 수행합니다.
 * 주요 기능:
 * <ul>
 *   <li>이메일 또는 학생 코드로 사용자 조회</li>
 *   <li>사용자 존재 여부 확인</li>
 *   <li>사용자 저장</li>
 *   <li>비밀번호 업데이트</li>
 * </ul>
 * @author snowykte0426, jihoonwjj
 */
@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements MemberPersistencePort {

    private final MemberJpaRepository memberJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final MemberMapper memberMapper;

    /**
     * 이메일을 기준으로 사용자 정보를 조회합니다.
     * @param email 사용자 이메일
     * @return 도메인 사용자 객체
     * @throws MemberNotFoundException 사용자를 찾을 수 없는 경우
     */
    @Override
    public Member findMemberByEmail(String email) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(memberJpaEntity)
                        .where(memberJpaEntity.email.eq(email))
                        .fetchOne()
        ).map(memberMapper::toDomain).orElseThrow(MemberNotFoundException::new);
    }

    /**
     * 주어진 이메일을 가진 사용자가 존재하는지 확인합니다.
     * @param email 사용자 이메일
     * @return 존재 여부
     */
    @Override
    public Boolean existsMemberByEmail(String email) {
        var result = jpaQueryFactory
                .selectOne()
                .from(memberJpaEntity)
                .where(memberJpaEntity.email.eq(email))
                .fetchFirst();
        return result != null;
    }

    /**
     * 사용자 정보를 저장합니다.
     * @param member 저장할 사용자 도메인 객체
     * @return 저장된 사용자 도메인 객체
     */
    @Override
    public Member saveMember(Member member) {
        return memberMapper.toDomain(memberJpaRepository.save(memberMapper.toEntity(member)));
    }

    /**
     * 사용자의 비밀번호를 갱신합니다.
     * @param memberId 사용자 ID
     * @param newPassword 새 비밀번호
     */
    @Override
    public void updateMemberPassword(Long memberId, String newPassword) {
        jpaQueryFactory
                .update(memberJpaEntity)
                .set(memberJpaEntity.password, newPassword)
                .where(memberJpaEntity.id.eq(memberId))
                .execute();
    }
}