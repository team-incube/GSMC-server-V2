package team.incude.gsmc.v2.domain.member.application;

import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.member.application.port.MemberApplicationPort;
import team.incude.gsmc.v2.domain.member.application.usecase.FindAllStudentUseCase;
import team.incude.gsmc.v2.domain.member.presentation.data.response.GetStudentResponse;
import team.incude.gsmc.v2.domain.member.presentation.data.response.SearchStudentResponse;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;

@Adapter(direction = PortDirection.INBOUND)
@RequiredArgsConstructor
public class MemberApplicationAdapter implements MemberApplicationPort {

    private final FindAllStudentUseCase findAllStudentUseCase;

    @Override
    public List<GetStudentResponse> findAllStudents() {
        return findAllStudentUseCase.getAllStudents();
    }

    @Override
    public SearchStudentResponse searchStudents(String name, Integer grade, Integer classNumber, Integer page, Integer size) {
        return null;
    }

    @Override
    public GetStudentResponse findCurrentStudent() {
        return null;
    }

    @Override
    public GetStudentResponse findMemberByStudentCode(String studentCode) {
        return null;
    }
}