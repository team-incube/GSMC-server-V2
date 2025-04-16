package team.incude.gsmc.v2.domain.auth.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

public class MemberExistException extends GsmcException {
    public MemberExistException() {
        super(ErrorCode.MEMBER_ALREADY_EXISTS);
    }
}
