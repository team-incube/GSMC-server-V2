package team.incude.gsmc.v2.domain.evidence.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

public class DraftActivityEvidenceNotFoundException extends GsmcException {
    public DraftActivityEvidenceNotFoundException() {
        super(ErrorCode.DRAFT_ACTIVITY_EVIDENCE_NOT_FOUND);
    }
}
