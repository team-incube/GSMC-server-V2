package team.incude.gsmc.v2.domain.member.application.usecase;

import team.incude.gsmc.v2.domain.member.presentation.data.response.GetMemberResponse;

import java.util.List;

public interface FindAllMemberUseCase {
    List<GetMemberResponse> getAllMembers();
}