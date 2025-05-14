package team.incude.gsmc.v2.global.thirdparty.email.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

/**
 * 이메일 발송에 실패했을 때 발생하는 예외입니다.
 * <p>SMTP 오류, 네트워크 장애, 인증 오류 등으로 인해 이메일을 정상적으로 전송할 수 없을 경우 이 예외가 발생합니다.
 * {@link team.incude.gsmc.v2.global.error.ErrorCode#EMAIL_SEND_FAILED}에 매핑되어 클라이언트에 적절한 오류 메시지를 전달합니다.
 * 이 예외는 인증 코드 발송, 비밀번호 초기화 등 메일 발송이 필요한 모든 유스케이스에서 사용될 수 있습니다.
 * @author jihoonwjj
 */
public class EmailSendFailedException extends GsmcException {
    public EmailSendFailedException() {
        super(ErrorCode.EMAIL_SEND_FAILED);
    }
}