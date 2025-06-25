package team.incube.gsmc.v2.global.thirdparty.aws.exception;

import team.incube.gsmc.v2.global.thirdparty.aws.s3.usecase.service.FileUploadService;
import team.incube.gsmc.v2.global.error.ErrorCode;
import team.incube.gsmc.v2.global.error.exception.GsmcException;

/**
 * AWS S3에 파일 업로드가 실패했을 때 발생하는 예외입니다.
 * <p>파일 스트림 처리 오류, 네트워크 장애, 권한 문제 등으로 인해 업로드 작업이 정상적으로 완료되지 않은 경우 사용됩니다.
 * {@link ErrorCode#S3_UPLOAD_FAILED}에 매핑되어 클라이언트에게 업로드 실패 사유를 명확히 전달합니다.
 * <p>주로 {@link FileUploadService}에서 파일 업로드 중 예외 발생 시 던져집니다.
 * @author snowykte0426
 */
public class S3UploadFailedException extends GsmcException {
    public S3UploadFailedException() {
        super(ErrorCode.S3_UPLOAD_FAILED);
    }
}