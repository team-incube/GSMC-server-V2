package team.incude.gsmc.v2.domain.certificate.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

public class DuplicateCertificateException extends GsmcException {
    public DuplicateCertificateException() {
        super(ErrorCode.DUPLICATE_CERTIFICATE);
    }
}