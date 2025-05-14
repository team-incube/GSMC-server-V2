package team.incude.gsmc.v2.domain.auth.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;


/**
 * 이메일 인증 시도 횟수를 초과했을 때 발생하는 예외입니다.
 * <p>Redis에 저장된 인증 요청 시도 횟수가 설정된 제한을 초과한 경우 발생합니다.
 * 보안 상의 이유로 일정 횟수 이상 인증을 시도한 사용자의 이메일 인증을 제한합니다.
 * <p>{@link team.incude.gsmc.v2.global.error.ErrorCode#EMAIL_AUTH_ATTEMPT_EXCEEDED}에 매핑되어
 * 클라이언트에 적절한 오류 메시지와 상태 코드를 전달합니다.
 * @see team.incude.gsmc.v2.domain.auth.application.usecase.service.SendAuthenticationEmailService
 * @author jihoonwjj
 */
public class EmailAuthAttemptExceededException extends GsmcException {
    public EmailAuthAttemptExceededException() {
        super(ErrorCode.EMAIL_AUTH_ATTEMPT_EXCEEDED);
    }
}
