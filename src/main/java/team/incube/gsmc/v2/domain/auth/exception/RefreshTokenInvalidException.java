package team.incube.gsmc.v2.domain.auth.exception;

import team.incube.gsmc.v2.domain.auth.application.usecase.RefreshUseCase;
import team.incube.gsmc.v2.domain.auth.application.usecase.service.RefreshService;
import team.incube.gsmc.v2.global.error.ErrorCode;
import team.incube.gsmc.v2.global.error.exception.GsmcException;

/**
 * 리프레시 토큰이 유효하지 않을 경우 발생하는 예외입니다.
 * <p>토큰이 만료되었거나, 변조되었거나, 저장소에 존재하지 않는 경우 인증 재발급이 거부되며 이 예외가 발생합니다.
 * <p>{@link ErrorCode#REFRESH_TOKEN_INVALID}에 매핑되어
 * 클라이언트에 적절한 오류 메시지를 전달합니다.
 * @see RefreshService
 * @see RefreshUseCase
 * author jihoonwjj
 */
public class RefreshTokenInvalidException extends GsmcException {
    public RefreshTokenInvalidException() {
        super(ErrorCode.REFRESH_TOKEN_INVALID);
    }
}