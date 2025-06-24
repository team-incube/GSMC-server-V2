package team.incube.gsmc.v2.domain.certificate.exception;

import team.incube.gsmc.v2.global.error.ErrorCode;
import team.incube.gsmc.v2.global.error.exception.GsmcException;

/**
 * 주어진 ID에 해당하는 자격증이 존재하지 않을 때 발생하는 예외입니다.
 * <p>자격증 조회, 수정, 삭제 요청 시 지정된 자격증이 데이터베이스에 존재하지 않는 경우 발생합니다.
 * {@link ErrorCode#CERTIFICATE_NOT_FOUND} 에 매핑됩니다.
 * 이 예외는 주로 {@code Optional.orElseThrow()} 또는 명시적인 존재 검사 후 발생하며,
 * 클라이언트에게 404 Not Found 에러를 전달하는 데 사용됩니다.
 * @author snowykte0426
 */
public class CertificateNotFoundException extends GsmcException {
    public CertificateNotFoundException() {
        super(ErrorCode.CERTIFICATE_NOT_FOUND);
    }
}