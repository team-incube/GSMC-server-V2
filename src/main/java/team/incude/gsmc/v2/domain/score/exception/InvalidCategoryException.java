package team.incude.gsmc.v2.domain.score.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

public class InvalidCategoryException extends GsmcException {
    public InvalidCategoryException() {
        super(ErrorCode.INVALID_CATEGORY);
    }
}