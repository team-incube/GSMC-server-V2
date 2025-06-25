package team.incube.gsmc.v2.domain.auth.exception;

import team.incube.gsmc.v2.domain.auth.application.usecase.SignInUseCase;
import team.incube.gsmc.v2.domain.auth.application.usecase.service.SignInService;
import team.incube.gsmc.v2.global.error.ErrorCode;
import team.incube.gsmc.v2.global.error.exception.GsmcException;

/**
 * 로그인 시 입력된 비밀번호가 일치하지 않을 경우 발생하는 예외입니다.
 *
 * <p>비밀번호 비교 과정에서 {@link org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder#matches(CharSequence, String)}
 * 결과가 false일 때 발생하며, 인증 실패로 간주되어 로그인에 실패합니다.
 * <p>{@link ErrorCode#PASSWORD_INVALID}에 매핑되어 클라이언트에 적절한 오류 메시지를 전달합니다.
 * @see SignInService
 * @see SignInUseCase
 * @author jihoonwjj
 */
public class PasswordInvalidException extends GsmcException {
    public PasswordInvalidException() {
        super(ErrorCode.PASSWORD_INVALID);
    }
}
