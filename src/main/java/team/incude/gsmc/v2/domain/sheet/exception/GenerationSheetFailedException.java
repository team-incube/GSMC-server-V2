package team.incude.gsmc.v2.domain.sheet.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

/**
 * 시트 생성 중 오류가 발생했을 때 던져지는 예외입니다.
 * <p>엑셀 또는 시트 파일을 생성하는 과정에서 IOException, 파일 포맷 오류 등 예기치 못한 문제가 발생할 경우 사용됩니다.
 * {@link ErrorCode#SHEET_GENERATION_FAILED}에 매핑되어 클라이언트에게 내부 서버 오류를 알립니다.
 * <p>일반적으로 {@code SheetApplicationPort} 또는 관련 서비스 계층에서 발생합니다.
 * @author snowykte0426
 */
public class GenerationSheetFailedException extends GsmcException {
    public GenerationSheetFailedException() {
        super(ErrorCode.SHEET_GENERATION_FAILED);
    }
}