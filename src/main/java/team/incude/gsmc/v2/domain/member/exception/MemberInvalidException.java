package team.incude.gsmc.v2.domain.member.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

public class MemberInvalidException extends GsmcException {
    public MemberInvalidException() {
        super(ErrorCode.MEMBER_INVALID);
    }
}
