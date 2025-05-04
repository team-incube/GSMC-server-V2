package team.incude.gsmc.v2.domain.member.application.usecase;

import team.incude.gsmc.v2.domain.member.presentation.data.response.GetStudentResponse;

/**
 * 현재 로그인한 학생 정보를 조회하는 유스케이스를 정의하는 인터페이스입니다.
 * <p>인증 정보를 기반으로 현재 사용자의 이메일 또는 세션을 확인하고,
 * 해당 사용자에 대한 상세 학생 정보를 {@link GetStudentResponse} 형태로 반환합니다.
 * 이 유스케이스는 로그인된 학생의 개인정보와 점수 상태 등을 클라이언트에 제공하기 위해 사용됩니다.
 * @author snowykte0426
 */
public interface FindCurrentStudentUseCase {
    GetStudentResponse execute();
}