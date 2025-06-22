package team.incube.gsmc.v2.domain.member.persistence.projection;

import team.incube.gsmc.v2.domain.member.domain.constant.MemberRole;

/**
 * QueryDSL 또는 Native Query에서 학생 정보를 일부만 조회할 때 사용하는 Projection 클래스입니다.
 * <p>전체 엔티티가 아닌 필요한 필드만 선별적으로 조회하여 성능을 최적화하고,
 * {@code StudentDetail} 및 {@code Member}의 결합된 정보를 표현합니다.
 * <ul>
 *   <li>{@code email} - 학생 이메일</li>
 *   <li>{@code name} - 학생 이름</li>
 *   <li>{@code grade} - 학년</li>
 *   <li>{@code classNumber} - 반 번호</li>
 *   <li>{@code number} - 출석 번호</li>
 *   <li>{@code totalScore} - 총합 점수</li>
 *   <li>{@code hasPendingEvidence} - 증빙 검토 상태 여부</li>
 *   <li>{@code role} - 사용자 권한</li>
 * </ul>
 * 이 Projection은 주로 학생 목록 조회, 검색 API 등에서 사용됩니다.
 * @author snowykte0426
 */
public record StudentProjection(
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