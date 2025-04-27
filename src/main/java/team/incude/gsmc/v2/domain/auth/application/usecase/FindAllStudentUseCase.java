package team.incude.gsmc.v2.domain.member.application.usecase;

import team.incude.gsmc.v2.domain.member.presentation.data.response.GetStudentResponse;

import java.util.List;

/**
 * 전체 학생 목록을 조회하는 유스케이스를 정의하는 인터페이스입니다.
 * <p>현재 시스템에 등록된 모든 학생 정보를 조회하여 {@link GetStudentResponse} 리스트로 반환합니다.
 * 어드민 또는 관리자 화면에서 전체 학생을 표시할 때 사용됩니다.
 * @author snowykte0426
 */
public interface FindAllStudentUseCase {
    List<GetStudentResponse> execute();
}