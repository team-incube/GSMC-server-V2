package team.incude.gsmc.v2.domain.certificate.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

public class CertificateNotBelongToMemberException extends GsmcException {
    public CertificateNotBelongToMemberException() {
        super(ErrorCode.CERTIFICATE_NOT_BELONG_TO_MEMBER);
    }
}