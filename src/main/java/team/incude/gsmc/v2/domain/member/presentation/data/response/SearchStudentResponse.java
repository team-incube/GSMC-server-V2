package team.incude.gsmc.v2.domain.member.presentation.data.response;

import java.util.List;

public record SearchStudentResponse(
        Integer totalPage,
        Integer totalElements,
        List<GetStudentResponse> results
) {
}