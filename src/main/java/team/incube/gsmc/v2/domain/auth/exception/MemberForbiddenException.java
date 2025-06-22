package team.incube.gsmc.v2.domain.auth.exception;

import team.incube.gsmc.v2.global.error.ErrorCode;
import team.incube.gsmc.v2.global.error.exception.GsmcException;

/**
 * 사용자가 이메일 인증을 완료하지 않은 상태에서 민감한 작업을 시도할 때 발생하는 예외입니다.
 * <p>회원 가입 또는 비밀번호 변경 등의 작업을 수행하려면 이메일 인증이 선행되어야 하며,
 * 인증이 완료되지 않은 경우 {@link ErrorCode#MEMBER_FORBIDDEN}에 매핑된 이 예외가 발생합니다.
 * author jihoonwjj
 */
public class MemberForbiddenException extends GsmcException {
    public MemberForbiddenException() {
        super(ErrorCode.MEMBER_FORBIDDEN);
    }
}