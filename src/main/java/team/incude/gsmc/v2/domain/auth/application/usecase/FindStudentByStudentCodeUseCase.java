package team.incude.gsmc.v2.domain.member.application.usecase;

import team.incude.gsmc.v2.domain.member.presentation.data.response.GetStudentResponse;

/**
 * 학생 코드로 학생 정보를 조회하는 유스케이스를 정의하는 인터페이스입니다.
 * <p>학생 고유 코드({@code studentCode})를 기반으로 {@link GetStudentResponse} 형태의 학생 정보를 반환합니다.
 * 주로 어드민 또는 외부 시스템이 특정 학생의 상세 정보를 조회할 때 사용됩니다.
 * @see team.incude.gsmc.v2.domain.member.application.usecase.service.FindStudentByStudentCodeService
 * @see team.incude.gsmc.v2.domain.member.presentation.MemberWebAdapter
 * @see GetStudentResponse
 * @author snowykte0426
 */
public interface FindStudentByStudentCodeUseCase {
    GetStudentResponse execute(String studentCode);
}