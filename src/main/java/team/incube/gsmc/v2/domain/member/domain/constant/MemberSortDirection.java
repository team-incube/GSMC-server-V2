package team.incube.gsmc.v2.domain.member.domain.constant;

/**
 * 회원 정렬 방향을 정의하는 열거형입니다.
 * <p>회원 목록을 다양한 기준으로 정렬할 때 사용됩니다.
 * 각 상수는 정렬 기준과 방향을 나타냅니다.
 * @author snowykte0426
 */
public enum MemberSortDirection {
    TOTAL_SCORE_ASC,
    TOTAL_SCORE_DESC,

    NAME_ASC,
    NAME_DESC,

    GRADE_AND_CLASS_ASC,
    GRADE_AND_CLASS_DESC,

    STUDENT_CODE_ASC,
    STUDENT_CODE_DESC,
}