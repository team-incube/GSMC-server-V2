package team.incude.gsmc.v2.domain.member.persistence.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.member.domain.StudentDetail;
import team.incude.gsmc.v2.domain.member.persistence.entity.StudentDetailJpaEntity;
import team.incude.gsmc.v2.global.mapper.GenericMapper;

@Component
@RequiredArgsConstructor
public class StudentDetailMapper implements GenericMapper<StudentDetailJpaEntity, StudentDetail> {

    private final MemberMapper memberMapper;

    @Override
    public StudentDetailJpaEntity toEntity(StudentDetail studentDetail) {
        return StudentDetailJpaEntity.builder()
                .id(studentDetail.getId())
                .member(memberMapper.toEntity(studentDetail.getMember()))
                .grade(studentDetail.getGrade())
                .classNumber(studentDetail.getClassNumber())
                .number(studentDetail.getNumber())
                .totalScore(studentDetail.getTotalScore())
                .studentCode(studentDetail.getStudentCode())
                .build();
    }

    @Override
    public StudentDetail toDomain(StudentDetailJpaEntity studentDetailJpaEntity) {
        return StudentDetail.builder()
                .id(studentDetailJpaEntity.getId())
                .member(memberMapper.toDomain(studentDetailJpaEntity.getMember()))
                .grade(studentDetailJpaEntity.getGrade())
                .classNumber(studentDetailJpaEntity.getClassNumber())
                .number(studentDetailJpaEntity.getNumber())
                .totalScore(studentDetailJpaEntity.getTotalScore())
                .studentCode(studentDetailJpaEntity.getStudentCode())
                .build();
    }
}