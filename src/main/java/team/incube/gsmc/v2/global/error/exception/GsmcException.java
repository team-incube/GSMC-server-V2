package team.incube.gsmc.v2.global.error.exception;

import lombok.Getter;
import team.incube.gsmc.v2.global.error.handler.GlobalExceptionHandler;
import team.incube.gsmc.v2.global.error.ErrorCode;

/**
 * GSMC 애플리케이션의 공통 런타임 예외 클래스입니다.
 * <p>모든 커스텀 예외 클래스는 이 클래스를 상속받아 {@link ErrorCode}를 포함하도록 구성됩니다.
 * 이를 통해 예외 발생 시 일관된 메시지와 상태 코드를 전달할 수 있으며,
 * {@link GlobalExceptionHandler}에서 처리됩니다.
 * <p>이 예외는 메시지를 자동으로 {@code errorCode.getMessage()}로 설정합니다.
 * @author snowykte0426
 */
@Getter
public class GsmcException extends RuntimeException {
    private final ErrorCode errorCode;

    public GsmcException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}