package team.incude.gsmc.v2.domain.auth.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

public class EmailFormatInvalidException extends GsmcException {
  public EmailFormatInvalidException() {
    super(ErrorCode.EMAIL_FORMAT_INVALID);
  }
}
