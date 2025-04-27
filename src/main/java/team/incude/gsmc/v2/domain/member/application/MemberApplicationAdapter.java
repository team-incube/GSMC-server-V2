package team.incude.gsmc.v2.domain.member.application;

import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.member.application.port.MemberApplicationPort;
import team.incude.gsmc.v2.domain.member.presentation.data.response.GetMemberResponse;
import team.incude.gsmc.v2.domain.member.presentation.data.response.SearchMemberResponse;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;

@Adapter(direction = PortDirection.INBOUND)
@RequiredArgsConstructor
public class MemberApplicationAdapter implements MemberApplicationPort {


    @Override
    public List<GetMemberResponse> getAllStudents() {
        return List.of();
    }

    @Override
    public SearchMemberResponse searchStudents(String name, Integer grade, Integer classNumber, Integer page, Integer size) {
        return null;
    }

    @Override
    public GetMemberResponse getCurrentStudent() {
        return null;
    }

    @Override
    public GetMemberResponse getStudent(String studentCode) {
        return null;
    }
}