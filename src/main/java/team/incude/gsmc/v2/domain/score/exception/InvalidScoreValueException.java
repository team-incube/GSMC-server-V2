package team.incude.gsmc.v2.domain.score.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

/**
 * 유효하지 않은 점수 값이 전달되었을 때 발생하는 예외입니다.
 * <p>점수 값이 0 이하이거나, 논리적으로 유효하지 않은 상황에서 점수를 차감하려는 경우 등에 사용되며,
 * {@link ErrorCode#INVALID_SCORE_VALUE}에 매핑되어 클라이언트에 오류 응답을 전달합니다.
 * <p>주요 사용 예:
 * <ul>
 *   <li>점수 차감 시 값이 0 이하인 경우</li>
 *   <li>점수 연산이 비정상적으로 수행된 경우</li>
 * </ul>
 * @author snowykte0426
 */
public class InvalidScoreValueException extends GsmcException {
    public InvalidScoreValueException() {
        super(ErrorCode.INVALID_SCORE_VALUE);
    }
}