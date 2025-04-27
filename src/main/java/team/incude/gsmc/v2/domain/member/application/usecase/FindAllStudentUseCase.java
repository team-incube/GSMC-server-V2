package team.incude.gsmc.v2.domain.member.application.usecase;

import team.incude.gsmc.v2.domain.member.presentation.data.response.GetStudentResponse;

import java.util.List;

public interface FindAllStudentUseCase {
    List<GetStudentResponse> getAllStudents();
}