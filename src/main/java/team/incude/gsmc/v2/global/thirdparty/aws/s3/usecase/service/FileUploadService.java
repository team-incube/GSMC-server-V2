package team.incude.gsmc.v2.global.thirdparty.aws.s3.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import team.incude.gsmc.v2.global.thirdparty.aws.exception.S3UploadFailedException;
import team.incude.gsmc.v2.global.thirdparty.aws.s3.usecase.FileUploadUseCase;
import team.incude.gsmc.v2.global.util.FileValidationUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * 파일을 AWS S3에 비동기 방식으로 업로드하는 유스케이스 구현 클래스입니다.
 * <p>{@link FileUploadUseCase}를 구현하며, 업로드 대상 파일을 S3에 저장한 후 접근 가능한 URL을 반환합니다.
 * <p>처리 흐름:
 * <ul>
 *   <li>파일 이름에 UUID를 추가해 중복 방지</li>
 *   <li>{@link FileValidationUtil}을 통해 유효성 검증 수행</li>
 *   <li>{@link S3AsyncClient}를 사용해 비동기 업로드 실행</li>
 *   <li>업로드 완료 후, 접근 가능한 S3 URL을 반환</li>
 * </ul>
 * <p>예외 발생 시 {@link S3UploadFailedException}을 던지며, 파일 스트림에서의 오류 또는 S3 전송 실패를 감지합니다.
 * @author snowykte0426
 */
@Service
@RequiredArgsConstructor
public class FileUploadService implements FileUploadUseCase {

    private final S3AsyncClient s3AsyncClient;
    @Value("${aws.bucket.name}")
    private String bucketName;
    @Value("${aws.bucket.region}")
    private String region;

    @Override
    @Async
    public CompletableFuture<String> execute(String fileName, InputStream fileInputStream) {
        String uniqueFileName = UUID.randomUUID() + "/" + fileName;
        try {
            FileValidationUtil.validateFile(fileInputStream, bucketName, region, uniqueFileName);
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(uniqueFileName)
                    .build();
            AsyncRequestBody requestBody = AsyncRequestBody.fromBytes(fileInputStream.readAllBytes());
            CompletableFuture<PutObjectResponse> putObjectResponse = s3AsyncClient.putObject(putObjectRequest, requestBody);
            return putObjectResponse.thenApply(response -> String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, uniqueFileName));
        } catch (IOException e) {
            throw new S3UploadFailedException();
        }
    }
}