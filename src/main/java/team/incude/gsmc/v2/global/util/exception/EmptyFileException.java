package team.incude.gsmc.v2.global.util.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

public class EmptyFileException extends GsmcException {
    public EmptyFileException() {
        super(ErrorCode.FILE_IS_EMPTY);
    }
}