package team.incude.gsmc.v2.domain.member.application.usecase;

import team.incude.gsmc.v2.domain.member.presentation.data.response.GetStudentResponse;

import java.util.List;

/**
 * 모든 학생 정보를 조회하는 유스케이스 인터페이스입니다.
 * <p>학생 정보를 조회하여 {@link GetStudentResponse} 리스트로 반환합니다.
 * @author snowykte0426
 */
public interface FindAllStudentUseCase {
    List<GetStudentResponse> execute();
}