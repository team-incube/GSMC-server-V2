package team.incude.gsmc.v2.domain.member.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

public class HomeroomTeacherDetailNotFoundException extends GsmcException {
    public HomeroomTeacherDetailNotFoundException() {
        super(ErrorCode.HOMEROOM_TEACHER_DETAIL_NOT_FOUND);
    }
}
