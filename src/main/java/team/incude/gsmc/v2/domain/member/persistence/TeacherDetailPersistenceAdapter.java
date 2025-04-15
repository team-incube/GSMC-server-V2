package team.incude.gsmc.v2.domain.member.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.member.application.port.TeacherDetailPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.HomeroomTeacherDetail;
import team.incude.gsmc.v2.domain.member.exception.HomeroomTeacherDetailNotFoundException;
import team.incude.gsmc.v2.domain.member.persistence.mapper.HomeroomTeacherDetailMapper;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.Optional;

import static team.incude.gsmc.v2.domain.member.persistence.entity.QHomeroomTeacherDetailJpaEntity.homeroomTeacherDetailJpaEntity;
import static team.incude.gsmc.v2.domain.member.persistence.entity.QMemberJpaEntity.memberJpaEntity;

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class TeacherDetailPersistenceAdapter implements TeacherDetailPersistencePort {

    private final JPAQueryFactory jpaQueryFactory;
    private final HomeroomTeacherDetailMapper homeroomTeacherDetailMapper;

    @Override
    public HomeroomTeacherDetail findTeacherDetailByEmail(String email) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(homeroomTeacherDetailJpaEntity)
                .join(homeroomTeacherDetailJpaEntity.member, memberJpaEntity).fetchJoin()
                .where(memberJpaEntity.email.eq(email))
                .fetchOne()).map(homeroomTeacherDetailMapper::toDomain).orElseThrow(HomeroomTeacherDetailNotFoundException::new);
    }
}
