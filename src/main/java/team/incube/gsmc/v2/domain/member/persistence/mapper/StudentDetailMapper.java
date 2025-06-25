package team.incube.gsmc.v2.domain.member.persistence.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.incube.gsmc.v2.domain.member.domain.Member;
import team.incube.gsmc.v2.domain.member.domain.StudentDetail;
import team.incube.gsmc.v2.domain.member.domain.StudentDetailWithEvidence;
import team.incube.gsmc.v2.domain.member.persistence.entity.StudentDetailJpaEntity;
import team.incube.gsmc.v2.domain.member.persistence.projection.StudentProjection;
import team.incube.gsmc.v2.global.mapper.GenericMapper;

/**
 * 도메인 {@link StudentDetail}, {@link StudentDetailWithEvidence} 객체와
 * JPA 엔티티 {@link StudentDetailJpaEntity}, Projection {@link StudentProjection} 간의 매핑을 담당하는 클래스입니다.
 * <p>{@link GenericMapper}를 구현하여 도메인 계층과 영속성 계층 간의 의존성을 분리하고,
 * 일관된 변환 로직을 제공합니다.
 * <p>지원 기능:
 * <ul>
 *   <li>{@code toEntity} - 도메인 {@code StudentDetail} → JPA 엔티티로 변환</li>
 *   <li>{@code toDomain} - JPA 엔티티 → 도메인 {@code StudentDetail}로 변환</li>
 *   <li>{@code fromProjection} - Projection 객체 → {@code StudentDetailWithEvidence} 변환</li>
 * </ul>
 * 이 매퍼는 영속계층 Adapter 및 Projection 기반 조회 결과 처리에 사용됩니다.
 * @author snowykte0426, jihoonwjj
 */
@Component
@RequiredArgsConstructor
public class StudentDetailMapper implements GenericMapper<StudentDetailJpaEntity, StudentDetail> {

    private final MemberMapper memberMapper;

    /**
     * {@link StudentDetail} 객체를 {@link StudentDetailJpaEntity}로 변환합니다.
     * @param studentDetail 변환할 도메인 객체
     * @return 변환된 JPA 엔티티
     */
    @Override
    public StudentDetailJpaEntity toEntity(StudentDetail studentDetail) {
        return StudentDetailJpaEntity.builder()
                .id(studentDetail.getId())
                .member(
                        studentDetail.getMember() != null
                                ? memberMapper.toEntity(studentDetail.getMember())
                                : null
                )
                .grade(studentDetail.getGrade())
                .classNumber(studentDetail.getClassNumber())
                .number(studentDetail.getNumber())
                .totalScore(studentDetail.getTotalScore())
                .studentCode(studentDetail.getStudentCode())
                .build();
    }

    /**
     * {@link StudentDetailJpaEntity}를 {@link StudentDetail} 객체로 변환합니다.
     * @param studentDetailJpaEntity 변환할 JPA 엔티티
     * @return 변환된 도메인 객체
     */
    @Override
    public StudentDetail toDomain(StudentDetailJpaEntity studentDetailJpaEntity) {
        return StudentDetail.builder()
                .id(studentDetailJpaEntity.getId())
                .member(
                        studentDetailJpaEntity.getMember() != null
                                ? memberMapper.toDomain(studentDetailJpaEntity.getMember())
                                : null
                )
                .grade(studentDetailJpaEntity.getGrade())
                .classNumber(studentDetailJpaEntity.getClassNumber())
                .number(studentDetailJpaEntity.getNumber())
                .totalScore(studentDetailJpaEntity.getTotalScore())
                .studentCode(studentDetailJpaEntity.getStudentCode())
                .build();
    }

    /**
     * {@link StudentProjection} 객체를 {@link StudentDetailWithEvidence}로 변환합니다.
     * @param studentProjection 변환할 Projection 객체
     * @return 변환된 도메인 객체
     */
    public StudentDetailWithEvidence fromProjection(StudentProjection studentProjection) {
        return StudentDetailWithEvidence.builder()
                .studentDetail(
                        StudentDetail.builder()
                                .member(Member.builder()
                                        .email(studentProjection.email())
                                        .name(studentProjection.name())
                                        .role(studentProjection.role())
                                        .build()
                                )
                                .grade(studentProjection.grade())
                                .classNumber(studentProjection.classNumber())
                                .number(studentProjection.number())
                                .totalScore(studentProjection.totalScore())
                                .build()
                )
                .hasPendingEvidence(studentProjection.hasPendingEvidence())
                .build();
    }
}