package team.incude.gsmc.v2.global.security.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

public class MemberUnauthorizedException extends GsmcException {
    public MemberUnauthorizedException() {
        super(ErrorCode.MEMBER_UNAUTHORIZED);
    }
}