package team.incude.gsmc.v2.domain.evidence.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

public class DraftReadingEvidenceNotFoundException extends GsmcException {
    public DraftReadingEvidenceNotFoundException() {
        super(ErrorCode.DRAFT_READING_EVIDENCE_NOT_FOUND);
    }
}
