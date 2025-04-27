package team.incude.gsmc.v2.domain.member.application.usecase;

import team.incude.gsmc.v2.domain.member.presentation.data.response.SearchStudentResponse;

/**
 * 조건 기반 학생 검색 유스케이스를 정의하는 인터페이스입니다.
 * <p>학생의 이름, 학년, 반 번호를 기준으로 페이징 처리된 학생 목록을 조회합니다.
 * 이 유스케이스는 클라이언트가 검색 필터를 통해 학생을 조회할 수 있도록 지원합니다.
 * @see team.incude.gsmc.v2.domain.member.presentation.data.response.SearchStudentResponse
 * @see team.incude.gsmc.v2.domain.member.application.usecase.service.SearchStudentService
 * @author snowykte0426
 */
public interface SearchStudentUseCase {
    SearchStudentResponse execute(String name, Integer grade, Integer classNumber, Integer page, Integer size);
}