package team.incude.gsmc.v2.global.thirdparty.aws.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

public class FileNameTooLongException extends GsmcException {
  public FileNameTooLongException() {
    super(ErrorCode.FILE_NAME_TOO_LONG);
  }
}