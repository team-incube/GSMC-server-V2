package team.incude.gsmc.v2.global.thirdparty.aws.exception;

import team.incude.gsmc.v2.global.error.ErrorCode;
import team.incude.gsmc.v2.global.error.exception.GsmcException;

/**
 * AWS S3에서 파일 삭제에 실패했을 때 발생하는 예외입니다.
 * <p>비동기 삭제 작업 중 예기치 못한 오류가 발생하거나 S3 응답이 실패 상태인 경우에 이 예외가 사용됩니다.
 * {@link ErrorCode#S3_DELETE_FAILED}에 매핑되어 클라이언트에 적절한 오류 메시지를 전달합니다.
 * <p>주로 {@link team.incude.gsmc.v2.global.thirdparty.aws.s3.usecase.service.FileDeleteService} 내에서 S3 비동기 삭제 실패 시 발생합니다.
 * @author snowykte0426
 */
public class S3DeleteFailedException extends GsmcException {
    public S3DeleteFailedException() {
        super(ErrorCode.S3_DELETE_FAILED);
    }
}