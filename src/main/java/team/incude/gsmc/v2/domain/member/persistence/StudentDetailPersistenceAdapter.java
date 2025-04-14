package team.incude.gsmc.v2.domain.member.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.StudentDetail;
import team.incude.gsmc.v2.domain.member.exception.MemberInvalidException;
import team.incude.gsmc.v2.domain.member.persistence.mapper.StudentDetailMapper;
import team.incude.gsmc.v2.domain.member.persistence.repository.StudentDetailJpaRepository;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.Optional;

import static team.incude.gsmc.v2.domain.member.persistence.entity.QStudentDetailJpaEntity.studentDetailJpaEntity;

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class StudentDetailPersistenceAdapter implements StudentDetailPersistencePort {

    private final StudentDetailJpaRepository studentDetailJpaRepository;
    private final StudentDetailMapper studentDetailMapper;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public StudentDetail findStudentDetailByStudentCode(String studentCode) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(studentDetailJpaEntity)
                        .where(studentDetailJpaEntity.studentCode.eq(studentCode))
                        .fetchOne()
        ).map(studentDetailMapper::toDomain).orElseThrow(MemberInvalidException::new);
    }

    @Override
    public StudentDetail saveStudentDetail(StudentDetail studentDetail) {
        return studentDetailMapper.toDomain(studentDetailJpaRepository.save(studentDetailMapper.toEntity(studentDetail)));
    }
}
