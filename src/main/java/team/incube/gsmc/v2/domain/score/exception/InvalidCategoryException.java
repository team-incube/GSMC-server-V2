package team.incube.gsmc.v2.domain.score.exception;

import team.incube.gsmc.v2.global.error.ErrorCode;
import team.incube.gsmc.v2.global.error.exception.GsmcException;

/**
 * 점수 수정 또는 검증 과정에서 유효하지 않은 카테고리가 감지되었을 때 발생하는 예외입니다.
 * <p>카테고리 이름이 존재하지 않거나, 해당 작업에 적합하지 않은 경우 이 예외가 사용되며,
 * {@link ErrorCode#INVALID_CATEGORY}에 매핑되어 클라이언트에 적절한 오류 응답을 제공합니다.
 * <p>예시 상황:
 * <ul>
 *   <li>증빙이 필요한 카테고리인데 증빙 없이 수정 시도한 경우</li>
 *   <li>허용되지 않은 카테고리에 점수를 수정하려는 경우</li>
 * </ul>
 * @author snowykte0426
 */
public class InvalidCategoryException extends GsmcException {
    public InvalidCategoryException() {
        super(ErrorCode.INVALID_CATEGORY);
    }
}