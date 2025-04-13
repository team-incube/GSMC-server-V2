package team.incude.gsmc.v2.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // AWS S3
    S3_UPLOAD_FAILED("AWS S3 Upload Failed", HttpStatus.INTERNAL_SERVER_ERROR.value()),
    S3_DELETE_FAILED("AWS S3 Delete Failed", HttpStatus.INTERNAL_SERVER_ERROR.value()),

    // Certificate
    CERTIFICATE_NOT_FOUND("Certificate Not Found", HttpStatus.NOT_FOUND.value()),

    // Score
    SCORE_MAXIMUM_VALUE_EXCEEDED("Score Maximum Value Exceeded", HttpStatus.UNPROCESSABLE_ENTITY.value()),

    // Member
    MEMBER_NOT_FOUND("Member Not Found", HttpStatus.NOT_FOUND.value()),
    MEMBER_ALREADY_EXISTS("Member Already Exists", HttpStatus.CONFLICT.value()),

    // Category
    CATEGORY_NOT_FOUND("Category Not Found", HttpStatus.NOT_FOUND.value()),

    // Auth
    MEMBER_UNAUTHORIZED("Member Not Authorized", HttpStatus.UNAUTHORIZED.value()),
    MEMBER_FORBIDDEN("Member Forbidden", HttpStatus.FORBIDDEN.value()),
    REFRESH_TOKEN_INVALID("RefreshToken Expired Or Invalid", HttpStatus.UNAUTHORIZED.value()),
    EMAIL_AUTH_ATTEMPT_EXCEEDED("Email Auth Attempt Exceeded", HttpStatus.TOO_MANY_REQUESTS.value()),
    PASSWORD_INVALID("Password Invalid", HttpStatus.UNAUTHORIZED.value()),
    VERIFICATION_INVALID("Verification Invalid", HttpStatus.UNAUTHORIZED.value()),

    // Evidence
    EVIDENCE_NOT_FOUND("Evidence Not Found", HttpStatus.NOT_FOUND.value());

    private final String message;
    private final int status;
}