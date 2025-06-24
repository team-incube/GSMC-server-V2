package team.incube.gsmc.v2.domain.auth.exception;

import team.incube.gsmc.v2.domain.auth.application.port.AuthenticationPersistencePort;
import team.incube.gsmc.v2.domain.auth.persistence.AuthenticationPersistenceAdapter;
import team.incube.gsmc.v2.global.error.ErrorCode;
import team.incube.gsmc.v2.global.error.exception.GsmcException;

/**
 * 이메일 인증 정보가 존재하지 않을 경우 발생하는 예외입니다.
 * <p>Redis에서 인증 정보를 조회했으나
 * 해당 이메일에 대한 인증 객체가 존재하지 않을 때 이 예외가 던져집니다.
 * <p>{@link ErrorCode#AUTHENTICATION_NOT_FOUND}에 매핑되어
 * 클라이언트에 적절한 오류 메시지를 전달합니다.
 * @see AuthenticationPersistenceAdapter
 * @see AuthenticationPersistencePort
 * @author jihoonwjj
 */
public class AuthenticationNotFoundException extends GsmcException {
    public AuthenticationNotFoundException() {
        super(ErrorCode.AUTHENTICATION_NOT_FOUND);
    }
}
