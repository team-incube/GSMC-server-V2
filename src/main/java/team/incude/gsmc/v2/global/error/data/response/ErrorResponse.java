package team.incude.gsmc.v2.global.error.data.response;


/**
 * API 오류 응답 본문을 나타내는 레코드입니다.
 * <p>클라이언트에게 전달되는 표준 오류 메시지 형식으로,
 * 오류 메시지와 HTTP 상태 코드를 포함합니다.
 * 이 응답 객체는 {@link team.incude.gsmc.v2.global.error.handler.GlobalExceptionHandler}에서 예외 처리 시 반환됩니다.
 * @param message 클라이언트에 전달할 오류 메시지
 * @param httpStatus HTTP 상태 코드
 * @author snowykte0426
 */
public record ErrorResponse(String message, int httpStatus) {
}