package team.incude.gsmc.v2.domain.certificate.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

public class CertificateCategoryMismatchException extends GsmcException {
    public CertificateCategoryMismatchException() {
        super(ErrorCode.CERTIFICATE_CATEGORY_MISMATCH);
    }
}