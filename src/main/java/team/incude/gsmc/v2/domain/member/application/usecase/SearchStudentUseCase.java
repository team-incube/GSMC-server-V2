package team.incude.gsmc.v2.domain.member.application.usecase;

import team.incude.gsmc.v2.domain.member.presentation.data.response.SearchStudentResponse;

public interface SearchStudentUseCase {
    SearchStudentResponse execute(String name, Integer grade, Integer classNumber, Integer page, Integer size);
}