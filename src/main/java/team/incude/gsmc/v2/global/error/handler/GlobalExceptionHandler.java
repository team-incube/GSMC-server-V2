package team.incude.gsmc.v2.global.error.handler;

import org.hibernate.NonUniqueResultException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import team.incude.gsmc.v2.global.error.data.response.ErrorResponse;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GsmcException.class)
    public ResponseEntity<ErrorResponse> handleGroomException(GsmcException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(new ErrorResponse(e.getErrorCode().getMessage(), e.getErrorCode().getStatus()));
    }

    @ExceptionHandler(NonUniqueResultException.class)
    public ResponseEntity<ErrorResponse> handleNonUniqueResultException(NonUniqueResultException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("Duplicate results detected, Please check your request", HttpStatus.CONFLICT.value()));
    }
}