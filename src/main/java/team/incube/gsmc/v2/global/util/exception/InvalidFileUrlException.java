package team.incube.gsmc.v2.global.util.exception;

import team.incube.gsmc.v2.global.error.ErrorCode;
import team.incube.gsmc.v2.global.error.exception.GsmcException;

/**
 * 유효하지 않은 파일 URI가 감지되었을 때 발생하는 예외입니다.
 * <p>파일 삭제, 접근 등의 과정에서 URI 형식이 잘못되었거나 유효하지 않을 경우 이 예외가 발생합니다.
 * {@link ErrorCode#INVALID_FILE_URI}에 매핑되어 클라이언트에게 적절한 오류 응답을 제공합니다.
 * <p>주로 {@code ExtractFileKeyUtil} 등의 파일 관련 유틸리티에서 사용됩니다.
 * @author snowykte0426
 */
public class InvalidFileUrlException extends GsmcException {
    public InvalidFileUrlException() {
        super(ErrorCode.INVALID_FILE_URI);
    }
}