package team.incude.gsmc.v2.domain.evidence.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;


/**
 * 활동 증빙자료가 존재하지 않을 때 발생하는 예외입니다.
 * <p>예: 삭제되었거나 존재하지 않는 ID로 조회를 시도한 경우
 * <p>예외 발생 시 {@link team.incude.gsmc.v2.global.error.ErrorCode#ACTIVITY_EVIDENCE_NOT_FOUNT} 코드가 반환됩니다.
 * @author suuuuuuminnnnnn
 */
public class ActivityEvidenceNotFountException extends GsmcException {
    public ActivityEvidenceNotFountException() {
      super(ErrorCode.ACTIVITY_EVIDENCE_NOT_FOUNT);
    }
}
