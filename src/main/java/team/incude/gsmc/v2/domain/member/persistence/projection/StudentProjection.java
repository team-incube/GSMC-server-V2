package team.incude.gsmc.v2.domain.member.persistence.projection;

import team.incude.gsmc.v2.domain.member.domain.constant.MemberRole;

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