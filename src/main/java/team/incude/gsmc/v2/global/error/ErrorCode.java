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
    FILE_NAME_TOO_LONG("File Name Too Long", HttpStatus.BAD_REQUEST.value()),
    FILE_IS_EMPTY("File is Empty", HttpStatus.BAD_REQUEST.value()),
    INVALID_FILE_URI("Invalid Stored File URI", HttpStatus.INTERNAL_SERVER_ERROR.value()),

    // Certificate
    CERTIFICATE_NOT_FOUND("Certificate Not Found", HttpStatus.NOT_FOUND.value()),
    CERTIFICATE_NOT_BELONG_TO_MEMBER("Certificate Not Belong To Member", HttpStatus.FORBIDDEN.value()),

    // Score
    SCORE_MAXIMUM_VALUE_EXCEEDED("Score Maximum Value Exceeded", HttpStatus.UNPROCESSABLE_ENTITY.value()),
    INVALID_SCORE_VALUE("Invalid Score Value", HttpStatus.UNPROCESSABLE_ENTITY.value()),

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
    AUTHENTICATION_NOT_FOUND("Authentication Object not found", HttpStatus.FORBIDDEN.value()),
    MEMBER_INVALID("No Member Found", HttpStatus.UNAUTHORIZED.value()),

    // Email Authentication
    EMAIL_FORMAT_INVALID("Email Format Invalid", HttpStatus.UNAUTHORIZED.value()),
    VERIFICATION_INVALID("Verification Invalid", HttpStatus.UNAUTHORIZED.value()),

    // Evidence
    EVIDENCE_NOT_FOUND("Evidence Not Found", HttpStatus.NOT_FOUND.value()),

    // Activity Evidence
    ACTIVITY_EVIDENCE_NOT_FOUNT("Activity Evidence Not Found", HttpStatus.NOT_FOUND.value()),

    // Other Evidence
    OTHER_EVIDENCE_NOT_FOUND("Other Evidence Not Found", HttpStatus.NOT_FOUND.value()),

    // Reading Evidence
    READING_EVIDENCE_NOT_FOUND("Reading Evidence Not Found", HttpStatus.NOT_FOUND.value());

    private final String message;
    private final int status;
}