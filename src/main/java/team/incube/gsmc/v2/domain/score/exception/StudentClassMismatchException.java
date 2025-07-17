package team.incube.gsmc.v2.domain.score.exception;

import team.incube.gsmc.v2.global.error.ErrorCode;
import team.incube.gsmc.v2.global.error.exception.GsmcException;

public class StudentClassMismatchException extends GsmcException {
    public StudentClassMismatchException() {
        super(ErrorCode.INVALID_STUDENT_GROUP);
    }
}
