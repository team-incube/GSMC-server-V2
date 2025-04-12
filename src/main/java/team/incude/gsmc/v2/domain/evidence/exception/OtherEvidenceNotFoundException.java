package team.incude.gsmc.v2.domain.evidence.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

public class OtherEvidenceNotFoundException extends GsmcException {
    public OtherEvidenceNotFoundException() {
        super(ErrorCode.OTHER_EVIDENCE_NOT_FOUND);
    }
}
