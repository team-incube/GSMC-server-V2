package team.incube.gsmc.v2.domain.score.application.usecase;

import team.incube.gsmc.v2.domain.score.presentation.data.response.GetScoreResponse;

/**
 * 점수 조회 유스케이스를 정의하는 인터페이스입니다.
 * <p>현재 로그인한 사용자 또는 특정 학생의 점수를 조회할 수 있는 진입점을 제공합니다.
 * 각각의 요청은 {@link GetScoreResponse} 형태로 점수 데이터를 반환합니다.
 * <ul>
 *   <li>{@code execute()} - 현재 로그인한 사용자의 점수 조회</li>
 *   <li>{@code execute(String email)} - 특정 학생 코드로 점수 조회</li>
 * </ul>
 * @author snowykte0426
 */
public interface FindScoreUseCase {
    GetScoreResponse execute();

    GetScoreResponse execute(String email);
}