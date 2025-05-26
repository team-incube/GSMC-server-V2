package team.incude.gsmc.v2.global.error.handler;

import org.hibernate.NonUniqueResultException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import team.incude.gsmc.v2.global.error.data.response.ErrorResponse;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

/**
 * 애플리케이션 전역에서 발생하는 예외를 처리하는 예외 핸들러입니다.
 * <p>{@code @ControllerAdvice}를 통해 모든 컨트롤러에서 발생하는 예외를 가로채고,
 * 정의된 예외 타입에 따라 적절한 HTTP 응답을 반환합니다.
 * <p>지원하는 예외:
 * <ul>
 *   <li>{@link GsmcException} - 커스텀 예외 및 도메인 전용 예외</li>
 *   <li>{@link NonUniqueResultException} - JPA 조회 시 결과 중복 예외</li>
 *   <li>{@link DataIntegrityViolationException} - 데이터베이스 무결성 제약 조건 위반 예외</li>
 * </ul>
 * 응답 형식은 모두 {@link team.incude.gsmc.v2.global.error.data.response.ErrorResponse}로 통일되어 있습니다.
 * @author snowykte0426
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * {@link GsmcException} 처리 메서드.
     * <p>커스텀 예외에서 정의된 {@link team.incude.gsmc.v2.global.error.ErrorCode}를 기반으로
     * HTTP 상태 코드와 메시지를 포함한 오류 응답을 생성합니다.
     * @param e 처리할 GsmcException
     * @return 표준화된 오류 응답
     */
    @ExceptionHandler(GsmcException.class)
    public ResponseEntity<ErrorResponse> handleGroomException(GsmcException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(new ErrorResponse(e.getErrorCode().getMessage(), e.getErrorCode().getStatus()));
    }

    /**
     * {@link NonUniqueResultException} 처리 메서드.
     * <p>JPA 조회 시 단건 결과를 기대했지만 복수 결과가 반환된 경우 발생합니다.
     * HTTP 상태 코드 409(CONFLICT)와 함께 오류 메시지를 응답합니다.
     * @param e 처리할 NonUniqueResultException
     * @return 충돌 오류 응답
     */
    @ExceptionHandler(NonUniqueResultException.class)
    public ResponseEntity<ErrorResponse> handleNonUniqueResultException(NonUniqueResultException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("Duplicate results detected, Please check your request", HttpStatus.CONFLICT.value()));
    }

    /**
     * {@link DataIntegrityViolationException} 처리 메서드.
     * <p>데이터베이스 무결성 제약 조건 위반 시 발생하는 예외를 처리합니다.
     * 제약 조건 위반 유형에 따라 구체적인 오류 메시지를 제공합니다.
     * <ul>
     *   <li>자격증 중복 등록 - member_id와 name 복합 unique 제약 조건 위반</li>
     *   <li>기타 데이터 무결성 위반 - 외래 키 제약 조건 등</li>
     * </ul>
     * @param e 처리할 DataIntegrityViolationException
     * @return 적절한 오류 메시지와 HTTP 400(BAD_REQUEST) 상태 코드를 포함한 응답
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Data integrity violation occurred. Please check your request.", HttpStatus.BAD_REQUEST.value()));
    }
}