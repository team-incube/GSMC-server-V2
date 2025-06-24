package team.incube.gsmc.v2.domain.member.presentation.data.response;

import java.util.List;

/**
 * 학생 조건 검색 결과를 담는 응답 DTO입니다.
 * <p>페이징 처리된 결과로서, 전체 페이지 수와 총 학생 수, 조회된 학생 목록을 포함합니다.
 * <ul>
 *   <li>{@code totalPage} - 전체 페이지 수</li>
 *   <li>{@code totalElements} - 전체 학생 수</li>
 *   <li>{@code results} - 현재 페이지에 해당하는 학생 목록</li>
 * </ul>
 * 이 DTO는 주로 학생 검색 API ({@code /students/search}) 응답으로 사용됩니다.
 * @author snowykte0426
 */
public record SearchStudentResponse(
        Integer totalPage,
        Long totalElements,
        List<GetStudentResponse> results
) {
}