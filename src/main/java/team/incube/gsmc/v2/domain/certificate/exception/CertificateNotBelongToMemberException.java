package team.incube.gsmc.v2.domain.certificate.exception;

import team.incube.gsmc.v2.global.error.ErrorCode;
import team.incube.gsmc.v2.global.error.exception.GsmcException;

/**
 * 자격증이 해당 사용자에게 속하지 않았을 때 발생하는 예외입니다.
 * <p>예를 들어, 사용자가 다른 사람의 자격증을 조회, 수정 또는 삭제하려 할 때 이 예외가 발생합니다.
 * {@link ErrorCode#CERTIFICATE_NOT_BELONG_TO_MEMBER} 에 매핑됩니다.
 * 이 예외는 주로 자격증 관련 API에서 발생하며, 클라이언트에게 적절한 오류 메시지를 전달합니다.
 * @author snowykte0426
 */
public class CertificateNotBelongToMemberException extends GsmcException {
    public CertificateNotBelongToMemberException() {
        super(ErrorCode.CERTIFICATE_NOT_BELONG_TO_MEMBER);
    }
}