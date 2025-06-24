package team.incube.gsmc.v2.global.util.exception;

import team.incube.gsmc.v2.global.error.ErrorCode;
import team.incube.gsmc.v2.global.error.exception.GsmcException;
import team.incube.gsmc.v2.global.util.FileValidationUtil;

/**
 * S3 업로드 시 파일 이름(전체 URL 포함)이 허용된 길이를 초과한 경우 발생하는 예외입니다.
 * <p>파일 이름이 너무 길어 S3 URL 형식으로 구성할 때 {@code 255자}를 초과하면 이 예외가 발생합니다.
 * {@link ErrorCode#FILE_NAME_TOO_LONG}에 매핑되어 클라이언트에게 적절한 오류 메시지를 전달합니다.
 * <p>주로 {@link FileValidationUtil} 내부에서 검증 시 사용됩니다.
 * @author snowykte0426
 */
public class FileNameTooLongException extends GsmcException {
  public FileNameTooLongException() {
    super(ErrorCode.FILE_NAME_TOO_LONG);
  }
}