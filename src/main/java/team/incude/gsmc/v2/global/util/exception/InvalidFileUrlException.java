package team.incude.gsmc.v2.global.util.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

public class InvalidFileUrlException extends GsmcException {
    public InvalidFileUrlException() {
        super(ErrorCode.INVALID_FILE_URI);
    }
}