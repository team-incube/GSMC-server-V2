package team.incude.gsmc.v2.domain.member.application.port;

import team.incude.gsmc.v2.domain.member.presentation.data.response.GetMemberResponse;
import team.incude.gsmc.v2.domain.member.presentation.data.response.SearchMemberResponse;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.util.List;

@Port(direction = PortDirection.INBOUND)
public interface MemberApplicationPort {
    List<GetMemberResponse> getAllStudents();

    SearchMemberResponse searchStudents(String name, Integer grade, Integer classNumber, Integer page, Integer size);

    GetMemberResponse getCurrentStudent();

    GetMemberResponse getStudent(String studentCode);
}