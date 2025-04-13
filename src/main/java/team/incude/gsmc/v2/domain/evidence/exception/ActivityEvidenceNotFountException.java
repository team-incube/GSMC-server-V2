package team.incude.gsmc.v2.domain.evidence.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

public class ActivityEvidenceNotFountException extends GsmcException {
    public ActivityEvidenceNotFountException() {
      super(ErrorCode.ACTIVITY_EVIDENCE_NOT_FOUNT);
    }
}
