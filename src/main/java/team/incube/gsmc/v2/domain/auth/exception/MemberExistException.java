package team.incube.gsmc.v2.domain.auth.exception;

import team.incube.gsmc.v2.domain.auth.application.usecase.service.SignUpService;
import team.incube.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incube.gsmc.v2.global.error.ErrorCode;
import team.incube.gsmc.v2.global.error.exception.GsmcException;

/**
 * 이미 등록된 이메일로 회원 가입을 시도할 때 발생하는 예외입니다.
 * <p>회원 가입 과정에서 입력된 이메일이 기존에 등록된 사용자와 중복되는 경우 이 예외가 발생합니다.
 * <p>{@link ErrorCode#MEMBER_ALREADY_EXISTS}에 매핑되어
 * 클라이언트에 적절한 오류 메시지를 반환합니다.
 * @see SignUpService
 * @see MemberPersistencePort
 * @author jihoonwjj
 */
public class MemberExistException extends GsmcException {
    public MemberExistException() {
        super(ErrorCode.MEMBER_ALREADY_EXISTS);
    }
}
