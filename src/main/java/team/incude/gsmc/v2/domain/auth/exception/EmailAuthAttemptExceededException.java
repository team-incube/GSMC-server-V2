package team.incude.gsmc.v2.domain.auth.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

public class EmailAuthAttemptExceededException extends GsmcException {
    public EmailAuthAttemptExceededException() {
        super(ErrorCode.EMAIL_AUTH_ATTEMPT_EXCEEDED);
    }
}
