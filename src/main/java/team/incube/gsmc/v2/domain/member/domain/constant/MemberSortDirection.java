package team.incube.gsmc.v2.domain.member.domain.constant;

import com.querydsl.core.types.OrderSpecifier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static team.incube.gsmc.v2.domain.member.persistence.entity.QStudentDetailJpaEntity.studentDetailJpaEntity;

/**
 * 회원 정렬 방향을 정의하는 열거형입니다.
 * <p>회원 목록을 다양한 기준으로 정렬할 때 사용됩니다.
 * 각 상수는 정렬 기준과 방향을 나타내며, 정렬 필드와 방향 정보를 포함합니다.
 * @author snowykte0426
 */
@Getter
@RequiredArgsConstructor
public enum MemberSortDirection {
    TOTAL_SCORE_ASC("totalScore", "asc"),
    TOTAL_SCORE_DESC("totalScore", "desc"),

    NAME_ASC("name", "asc"),
    NAME_DESC("name", "desc"),

    GRADE_AND_CLASS_ASC("gradeAndClass", "asc"),
    GRADE_AND_CLASS_DESC("gradeAndClass", "desc"),

    STUDENT_CODE_ASC("studentCode", "asc"),
    STUDENT_CODE_DESC("studentCode", "desc");

    private final String field;
    private final String direction;
}