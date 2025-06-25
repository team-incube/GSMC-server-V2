package team.incube.gsmc.v2.domain.member.presentation.data.response;

import team.incube.gsmc.v2.domain.member.domain.constant.MemberRole;

/**
 * 학생 정보를 클라이언트에 전달하기 위한 응답 DTO입니다.
 * <p>학생의 기본 신상 정보(이메일, 이름, 학년/반/번호)와 함께,
 * 총점, 증빙자료 대기 여부, 권한 정보를 포함하여 반환됩니다.
 * <ul>
 *   <li>{@code email} - 학생 이메일</li>
 *   <li>{@code name} - 학생 이름</li>
 *   <li>{@code grade} - 학년</li>
 *   <li>{@code classNumber} - 반 번호</li>
 *   <li>{@code number} - 출석 번호</li>
 *   <li>{@code totalScore} - 총합 점수</li>
 *   <li>{@code hasPendingEvidence} - 증빙 대기 상태 여부</li>
 *   <li>{@code role} - 사용자의 권한</li>
 * </ul>
 * 이 DTO는 주로 {@code /students}, {@code /students/current}, {@code /students/{code}} 등에서 사용됩니다.
 * @author snowykte0426
 */

public record GetStudentResponse(
        String email,
        String name,
        Integer grade,
        Integer classNumber,
        Integer number,
        Integer totalScore,
        Boolean hasPendingEvidence,
        MemberRole role
) {
}