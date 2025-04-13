package team.incude.gsmc.v2.domain.certificate.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

public class CertificateNotFoundException extends GsmcException {
    public CertificateNotFoundException() {
        super(ErrorCode.CERTIFICATE_NOT_FOUND);
    }
}