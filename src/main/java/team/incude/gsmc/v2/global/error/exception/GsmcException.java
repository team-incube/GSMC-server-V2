package team.incude.gsmc.v2.global.error.exception;

import lombok.Getter;
import team.incude.gsmc.v2.global.error.ErrorCode;

@Getter
public class GsmcException extends RuntimeException {
    private final ErrorCode errorCode;

    public GsmcException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}