package team.incube.gsmc.v2.domain.evidence.exception;

import team.incube.gsmc.v2.global.error.ErrorCode;
import team.incube.gsmc.v2.global.error.exception.GsmcException;

public class ActivityEvidenceAccessDeniedException extends GsmcException {
    public ActivityEvidenceAccessDeniedException() {
        super(ErrorCode.ACTIVITY_EVIDENCE_ACCESS_DENIED);
    }
}
