package team.incude.gsmc.v2.domain.member.application.usecase;

import team.incude.gsmc.v2.domain.member.presentation.data.response.GetStudentResponse;

public interface FindStudentByStudentCodeUseCase {
    GetStudentResponse execute(String studentCode);
}