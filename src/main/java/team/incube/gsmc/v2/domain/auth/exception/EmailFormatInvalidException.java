package team.incube.gsmc.v2.domain.auth.exception;

import team.incube.gsmc.v2.global.error.ErrorCode;
import team.incube.gsmc.v2.global.error.exception.GsmcException;

/**
 * 이메일 형식이 유효하지 않을 경우 발생하는 예외입니다.
 * <p>회원 가입 또는 비밀번호 변경 등의 과정에서 입력된 이메일이
 * 형식 규칙("s학번@gsm.hs.kr")을 만족하지 않을 경우 이 예외가 발생합니다.
 * <p>{@link ErrorCode#EMAIL_FORMAT_INVALID}에 매핑되어
 * 클라이언트에 적절한 오류 메시지를 전달합니다.
 * @author jihoonwjj
 */
public class EmailFormatInvalidException extends GsmcException {
  public EmailFormatInvalidException() {
    super(ErrorCode.EMAIL_FORMAT_INVALID);
  }
}
