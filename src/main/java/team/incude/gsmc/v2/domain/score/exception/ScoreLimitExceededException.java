package team.incude.gsmc.v2.domain.score.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

public class ScoreLimitExceededException extends GsmcException {
    public ScoreLimitExceededException() {
        super(ErrorCode.SCORE_MAXIMUM_VALUE_EXCEEDED);
    }
}