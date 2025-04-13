package team.incude.gsmc.v2.domain.score.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

public class InvalidScoreValueException extends GsmcException {
    public InvalidScoreValueException() {
        super(ErrorCode.INVALID_SCORE_VALUE);
    }
}