package team.incube.gsmc.v2.domain.auth.exception;

import team.incube.gsmc.v2.global.error.ErrorCode;
import team.incube.gsmc.v2.global.error.exception.GsmcException;

/**
 * 유효하지 않거나 만료된 인증 코드로 이메일 인증을 시도할 때 발생하는 예외입니다.
 * <p>클라이언트가 제출한 인증 코드가 Redis 또는 저장소에 존재하지 않거나 유효하지 않을 경우 발생합니다.
 * 이 예외는 인증 실패로 간주되어 이메일 인증이 거부됩니다.
 * <p>{@link ErrorCode#VERIFICATION_INVALID}에 매핑되어
 * 클라이언트에 적절한 오류 메시지를 전달합니다.
 * @author jihoonwjj
 */
public class VerificationInvalidException extends GsmcException {
    public VerificationInvalidException() {
        super(ErrorCode.VERIFICATION_INVALID);
    }
}