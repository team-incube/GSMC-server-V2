package team.incude.gsmc.v2.domain.score.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

public class RequiredEvidenceCategoryException extends GsmcException {
    public RequiredEvidenceCategoryException() {
        super(ErrorCode.REQUIRED_EVIDENCE_CATEGORY);
    }
}