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

    // Category
    CATEGORY_NOT_FOUND("Category Not Found", HttpStatus.NOT_FOUND.value()),

    // Auth
    MEMBER_UNAUTHORIZED("Member Not Authorized", HttpStatus.UNAUTHORIZED.value());

    private final String message;
    private final int status;
}