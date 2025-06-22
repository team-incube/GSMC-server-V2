package team.incube.gsmc.v2.domain.member.application.usecase;

import team.incube.gsmc.v2.domain.member.presentation.data.response.GetStudentResponse;

/**
 * 현재 로그인한 학생의 정보를 조회하는 유스케이스 인터페이스입니다.
 * <p>현재 로그인한 학생의 정보를 {@link GetStudentResponse}로 반환합니다.
 * @author snowykte0426
 */
public interface FindStudentByEmailUseCase {
    GetStudentResponse execute(String studentCode);
}