package team.incude.gsmc.v2.global.thirdparty.aws.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

public class S3DeleteFailedException extends GsmcException {
    public S3DeleteFailedException() {
        super(ErrorCode.S3_DELETE_FAILED);
    }
}