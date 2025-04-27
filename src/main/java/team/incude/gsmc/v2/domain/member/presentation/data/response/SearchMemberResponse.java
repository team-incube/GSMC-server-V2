package team.incude.gsmc.v2.domain.member.presentation.data.response;

import java.util.List;

public record SearchMemberResponse(
        Integer totalPage,
        Integer totalElements,
        List<GetMemberResponse> results
) {
}