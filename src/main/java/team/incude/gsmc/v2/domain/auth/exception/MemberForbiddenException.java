package team.incude.gsmc.v2.domain.auth.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

public class MemberForbiddenException extends GsmcException {
    public MemberForbiddenException() {
        super(ErrorCode.MEMBER_FORBIDDEN);
    }
}