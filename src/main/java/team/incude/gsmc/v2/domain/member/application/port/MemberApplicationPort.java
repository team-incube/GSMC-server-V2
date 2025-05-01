package team.incude.gsmc.v2.domain.member.application.port;

import team.incude.gsmc.v2.domain.member.presentation.data.response.GetStudentResponse;
import team.incude.gsmc.v2.domain.member.presentation.data.response.SearchStudentResponse;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.util.List;

@Port(direction = PortDirection.INBOUND)
public interface MemberApplicationPort {
    List<GetStudentResponse> findAllStudents();

    SearchStudentResponse searchStudents(String name, Integer grade, Integer classNumber, Integer page, Integer size);

    GetStudentResponse findCurrentStudent();

    GetStudentResponse findMemberByStudentCode(String studentCode);
}