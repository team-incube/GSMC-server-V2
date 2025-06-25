package team.incube.gsmc.v2.domain.member.persistence.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.incube.gsmc.v2.domain.member.domain.HomeroomTeacherDetail;
import team.incube.gsmc.v2.domain.member.persistence.entity.HomeroomTeacherDetailJpaEntity;
import team.incube.gsmc.v2.global.mapper.GenericMapper;

/**
 * {@link HomeroomTeacherDetail} 도메인 객체와 {@link HomeroomTeacherDetailJpaEntity} JPA 엔티티 간의 매핑을 담당하는 클래스입니다.
 * <p>{@link GenericMapper}를 구현하며, 담임 교사 상세 정보의 저장/조회 시 객체 간 변환을 일관되게 수행합니다.
 * 내부적으로 {@link MemberMapper}를 활용하여 연관된 교사(Member) 정보도 함께 매핑합니다.
 * 주요 역할:
 * <ul>
 *   <li>도메인 객체를 JPA 엔티티로 변환하여 저장 가능하도록 구성</li>
 *   <li>JPA 엔티티를 도메인 객체로 변환하여 도메인 계층에서 활용</li>
 * </ul>
 * 이 매퍼는 영속계층 어댑터에서 사용됩니다.
 * @author snowykte0426, jihoonwjj
 */
@Component
@RequiredArgsConstructor
public class HomeroomTeacherDetailMapper implements GenericMapper<HomeroomTeacherDetailJpaEntity, HomeroomTeacherDetail> {

    private final MemberMapper memberMapper;

    /**
     * {@link HomeroomTeacherDetail} 객체를 {@link HomeroomTeacherDetailJpaEntity}로 변환합니다.
     * @param homeroomTeacherDetail 변환할 도메인 객체
     * @return 변환된 JPA 엔티티
     */
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

    /**
     * {@link HomeroomTeacherDetailJpaEntity}를 {@link HomeroomTeacherDetail} 객체로 변환합니다.
     * @param homeroomTeacherDetailJpaEntity 변환할 JPA 엔티티
     * @return 변환된 도메인 객체
     */
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