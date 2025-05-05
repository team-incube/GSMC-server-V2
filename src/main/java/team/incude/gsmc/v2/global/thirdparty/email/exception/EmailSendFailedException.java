package team.incude.gsmc.v2.global.thirdparty.email.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

public class EmailSendFailedException extends GsmcException {
    public EmailSendFailedException() {
        super(ErrorCode.EMAIL_SEND_FAILED);
    }
}