package team.incude.gsmc.v2.domain.score.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

/**
 * 증빙이 필요한 점수 카테고리에 대해 증빙자료 없이 점수를 수정하려 할 때 발생하는 예외입니다.
 * <p>예를 들어 자격증, 수상 등 증빙자료가 요구되는 항목에 대해 파일 없이 점수를 부여하려는 경우 발생합니다.
 * {@link ErrorCode#REQUIRED_EVIDENCE_CATEGORY}에 매핑되어 클라이언트에 적절한 오류 메시지를 전달합니다.
 * <p>이 예외는 점수 수정 로직에서 증빙 여부 검증 시 사용됩니다.
 * @author snowykte0426
 */
public class RequiredEvidenceCategoryException extends GsmcException {
    public RequiredEvidenceCategoryException() {
        super(ErrorCode.REQUIRED_EVIDENCE_CATEGORY);
    }
}