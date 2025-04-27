package team.incude.gsmc.v2.domain.member.presentation.data.response;

import team.incude.gsmc.v2.domain.member.domain.constant.MemberRole;

public record GetMemberResponse(
        String email,
        String name,
        Integer grade,
        Integer classNumber,
        Integer number,
        Integer totalScore,
        MemberRole role
) {
}