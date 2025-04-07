package team.incude.gsmc.v2.global.thirdparty.aws.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

public class S3UploadFailedException extends GsmcException {
    public S3UploadFailedException() {
        super(ErrorCode.S3_UPLOAD_FAILED);
    }
}