package team.incube.gsmc.v2.domain.auth.application.usecase;

import team.incube.gsmc.v2.domain.auth.presentation.data.response.AuthTokenResponse;

/**
 * 리프레시 토큰을 기반으로 새로운 액세스/리프레시 토큰을 발급하는 유스케이스입니다.
 * <p>클라이언트가 만료된 액세스 토큰을 갱신하고자 할 때 이 유스케이스를 실행합니다.
 * 전달된 리프레시 토큰이 유효한지 검증한 후, 새로운 토큰 쌍을 반환합니다.
 * 반환값은 {@link AuthTokenResponse} 형식이며, 토큰의 유효기간 및 사용자 권한 정보를 포함합니다.
 * @author jihoonwjj
 */
public interface RefreshUseCase {
    AuthTokenResponse execute(String refreshToken);
}
