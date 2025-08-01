package team.incube.gsmc.v2.domain.evidence.exception;

import team.incube.gsmc.v2.global.error.ErrorCode;
import team.incube.gsmc.v2.global.error.exception.GsmcException;

/**
 * 증빙자료가 존재하지 않을 때 발생하는 예외입니다.
 * <p>예: 주어진 ID로 증빙자료를 조회했지만 존재하지 않는 경우
 * <p>예외 발생 시 {@link ErrorCode#EVIDENCE_NOT_FOUND} 코드가 반환됩니다.
 * @author suuuuuuminnnnnn
 */
public class EvidenceNotFoundException extends GsmcException {
    public EvidenceNotFoundException() {
        super(ErrorCode.EVIDENCE_NOT_FOUND);
    }
}