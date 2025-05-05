package team.incude.gsmc.v2.domain.score.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

/**
 * 주어진 이름에 해당하는 점수 카테고리를 찾을 수 없을 때 발생하는 예외입니다.
 * <p>점수 조회 또는 수정 요청 시 유효하지 않은 카테고리 이름이 전달되었을 때 사용되며,
 * {@link ErrorCode#CATEGORY_NOT_FOUND}에 매핑되어 클라이언트에 적절한 오류 메시지를 제공합니다.
 * <p>이 예외는 주로 {@code CategoryPersistencePort.findCategoryByName()} 호출 시 발생할 수 있습니다.
 * @author snowykte0426
 */
public class CategoryNotFoundException extends GsmcException {
    public CategoryNotFoundException() {
        super(ErrorCode.CATEGORY_NOT_FOUND);
    }
}