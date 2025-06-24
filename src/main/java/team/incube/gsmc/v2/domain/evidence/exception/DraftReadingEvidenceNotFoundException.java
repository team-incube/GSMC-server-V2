package team.incube.gsmc.v2.domain.evidence.exception;

import team.incube.gsmc.v2.global.error.ErrorCode;
import team.incube.gsmc.v2.global.error.exception.GsmcException;

/**
 * 독서 증빙자료 임시저장이 존재하지 않을 때 발생하는 예외입니다.
 * <p>예: 주어진 UUID로 임시저장을 조회했지만 존재하지 않는 경우
 * <p>예외 발생 시 {@link ErrorCode#DRAFT_READING_EVIDENCE_NOT_FOUND} 코드가 반환됩니다.
 * @author suuuuuuminnnnnn
 */
public class DraftReadingEvidenceNotFoundException extends GsmcException {
    public DraftReadingEvidenceNotFoundException() {
        super(ErrorCode.DRAFT_READING_EVIDENCE_NOT_FOUND);
    }
}