package team.incube.gsmc.v2.domain.member.application.usecase;

import team.incube.gsmc.v2.domain.member.presentation.data.response.SearchStudentResponse;

/**
 * 학생을 검색하는 유스케이스 인터페이스입니다.
 * <p>학생의 이름, 학년, 반 번호를 기준으로 학생을 검색하여
 * {@link SearchStudentResponse}로 반환합니다.
 * @author snowykte0426
 */
public interface SearchStudentUseCase {
    SearchStudentResponse execute(String name, Integer grade, Integer classNumber, Integer page, Integer size);
}