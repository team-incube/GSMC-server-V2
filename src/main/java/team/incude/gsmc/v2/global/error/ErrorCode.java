package team.incude.gsmc.v2.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // AWS S3
    S3_UPLOAD_FAILED("AWS S3 Upload Failed", 500),
    S3_DELETE_FAILED("AWS S3 Delete Failed", 500);

    private final String message;
    private final int status;
}