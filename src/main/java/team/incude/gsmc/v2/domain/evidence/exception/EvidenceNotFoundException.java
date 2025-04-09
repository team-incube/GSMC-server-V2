package team.incude.gsmc.v2.domain.evidence.exception;

import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

public class EvidenceNotFoundException extends GsmcException {
    public EvidenceNotFoundException() {
        super(ErrorCode.EVIDENCE_NOT_FOUND);
    }
}