package team.incude.gsmc.v2.domain.member.application;

import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.member.application.port.MemberApplicationPort;
import team.incude.gsmc.v2.domain.member.application.usecase.FindAllStudentUseCase;
import team.incude.gsmc.v2.domain.member.application.usecase.FindCurrentStudentUseCase;
import team.incude.gsmc.v2.domain.member.application.usecase.FindStudentByStudentCodeUseCase;
import team.incude.gsmc.v2.domain.member.application.usecase.SearchStudentUseCase;
import team.incude.gsmc.v2.domain.member.presentation.data.response.GetStudentResponse;
import team.incude.gsmc.v2.domain.member.presentation.data.response.SearchStudentResponse;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;

@Adapter(direction = PortDirection.INBOUND)
@RequiredArgsConstructor
public class MemberApplicationAdapter implements MemberApplicationPort {

    private final FindAllStudentUseCase findAllStudentUseCase;
    private final SearchStudentUseCase searchStudentUseCase;
    private final FindCurrentStudentUseCase findCurrentStudentUseCase;
    private final FindStudentByStudentCodeUseCase findStudentByStudentCodeUseCase;

    @Override
    public List<GetStudentResponse> findAllStudents() {
        return findAllStudentUseCase.execute();
    }

    @Override
    public SearchStudentResponse searchStudents(String name, Integer grade, Integer classNumber, Integer page, Integer size) {
        return searchStudentUseCase.execute(name, grade, classNumber, page, size);
    }

    @Override
    public GetStudentResponse findCurrentStudent() {
        return findCurrentStudentUseCase.execute();
    }

    @Override
    public GetStudentResponse findMemberByStudentCode(String studentCode) {
        return findStudentByStudentCodeUseCase.execute(studentCode);
    }
}