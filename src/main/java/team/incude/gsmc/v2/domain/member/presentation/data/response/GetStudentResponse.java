package team.incude.gsmc.v2.domain.member.presentation.data.response;

import team.incude.gsmc.v2.domain.member.domain.constant.MemberRole;

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