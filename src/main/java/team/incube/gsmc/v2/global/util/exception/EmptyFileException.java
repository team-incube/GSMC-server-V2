package team.incube.gsmc.v2.global.util.exception;

import team.incube.gsmc.v2.global.error.ErrorCode;
import team.incube.gsmc.v2.global.error.exception.GsmcException;
import team.incube.gsmc.v2.global.util.FileValidationUtil;


/**
 * 업로드된 파일이 비어 있을 경우 발생하는 예외입니다.
 * <p>파일 스트림이 비어 있거나 null인 경우 유효성 검사에서 이 예외가 발생합니다.
 * {@link ErrorCode#FILE_IS_EMPTY}에 매핑되어 클라이언트에게 적절한 오류 메시지를 전달합니다.
 * <p>주로 {@link FileValidationUtil#validateFile}에서 사용됩니다.
 * @author snowykte0426
 */
public class EmptyFileException extends GsmcException {
    public EmptyFileException() {
        super(ErrorCode.FILE_IS_EMPTY);
    }
}