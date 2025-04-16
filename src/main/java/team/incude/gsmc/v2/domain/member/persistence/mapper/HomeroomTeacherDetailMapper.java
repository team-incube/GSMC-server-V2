package team.incude.gsmc.v2.domain.member.persistence.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.member.domain.HomeroomTeacherDetail;
import team.incude.gsmc.v2.domain.member.persistence.entity.HomeroomTeacherDetailJpaEntity;
import team.incude.gsmc.v2.global.mapper.GenericMapper;

@Component
@RequiredArgsConstructor
public class HomeroomTeacherDetailMapper implements GenericMapper<HomeroomTeacherDetailJpaEntity, HomeroomTeacherDetail> {

    private final MemberMapper memberMapper;

    @Override
    public HomeroomTeacherDetailJpaEntity toEntity(HomeroomTeacherDetail homeroomTeacherDetail) {
        return HomeroomTeacherDetailJpaEntity.builder()
                .id(homeroomTeacherDetail.getId())
                .member(
                        homeroomTeacherDetail.getMember() != null
                        ? memberMapper.toEntity(homeroomTeacherDetail.getMember())
                                : null
                )
                .grade(homeroomTeacherDetail.getGrade())
                .classNumber(homeroomTeacherDetail.getClassNumber())
                .build();
    }

    @Override
    public HomeroomTeacherDetail toDomain(HomeroomTeacherDetailJpaEntity homeroomTeacherDetailJpaEntity) {
        return HomeroomTeacherDetail.builder()
                .id(homeroomTeacherDetailJpaEntity.getId())
                .member(
                        homeroomTeacherDetailJpaEntity.getMember() != null
                        ? memberMapper.toDomain(homeroomTeacherDetailJpaEntity.getMember())
                                : null
                )
                .grade(homeroomTeacherDetailJpaEntity.getGrade())
                .classNumber(homeroomTeacherDetailJpaEntity.getClassNumber())
                .build();
    }
}