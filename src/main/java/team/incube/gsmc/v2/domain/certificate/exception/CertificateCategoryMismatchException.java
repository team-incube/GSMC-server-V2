package team.incube.gsmc.v2.domain.certificate.exception;

import team.incube.gsmc.v2.global.error.ErrorCode;
import team.incube.gsmc.v2.global.error.exception.GsmcException;

public class CertificateCategoryMismatchException extends GsmcException {
    public CertificateCategoryMismatchException() {
        super(ErrorCode.CERTIFICATE_CATEGORY_MISMATCH);
    }
}