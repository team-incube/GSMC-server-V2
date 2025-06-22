package team.incube.gsmc.v2.domain.score.exception;

import team.incube.gsmc.v2.global.error.ErrorCode;
import team.incube.gsmc.v2.global.error.exception.GsmcException;

/**
 * 점수가 허용된 최대값을 초과했을 때 발생하는 예외입니다.
 * <p>점수 등록 또는 수정 시 지정된 카테고리의 최대값을 초과하는 경우 이 예외가 발생합니다.
 * {@link ErrorCode#SCORE_MAXIMUM_VALUE_EXCEEDED}에 매핑되어 클라이언트에게 오류 응답을 제공합니다.
 * <p>예시 상황:
 * <ul>
 *   <li>최대값이 3인 카테고리에 4 이상의 점수를 부여하려는 경우</li>
 *   <li>유효성 검증 로직 또는 도메인 서비스 내에서 점수 상한 검출 시</li>
 * </ul>
 * @author snowykte0426
 */
public class ScoreLimitExceededException extends GsmcException {
    public ScoreLimitExceededException() {
        super(ErrorCode.SCORE_MAXIMUM_VALUE_EXCEEDED);
    }
}