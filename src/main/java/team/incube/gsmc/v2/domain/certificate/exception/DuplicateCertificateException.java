package team.incube.gsmc.v2.domain.certificate.exception;

import team.incube.gsmc.v2.global.error.ErrorCode;
import team.incube.gsmc.v2.global.error.exception.GsmcException;

public class DuplicateCertificateException extends GsmcException {
    public DuplicateCertificateException() {
        super(ErrorCode.DUPLICATE_CERTIFICATE);
    }
}