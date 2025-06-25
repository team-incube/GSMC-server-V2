package team.incube.gsmc.v2.global.thirdparty.aws.s3.usecase.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import team.incube.gsmc.v2.global.thirdparty.aws.data.AwsEnvironment;
import team.incube.gsmc.v2.global.thirdparty.aws.exception.S3DeleteFailedException;
import team.incube.gsmc.v2.global.thirdparty.aws.s3.usecase.FileDeleteUseCase;

import java.util.concurrent.CompletableFuture;

/**
 * AWS S3에서 파일을 삭제하는 유스케이스 구현 클래스입니다.
 * <p>{@link FileDeleteUseCase}를 구현하며, S3 버킷에 저장된 파일을 비동기 방식으로 삭제합니다.
 * <p>주요 기능:
 * <ul>
 *   <li>S3 버킷과 파일 키를 기반으로 삭제 요청 전송</li>
 *   <li>삭제 성공 시 로그 기록</li>
 *   <li>삭제 실패 시 {@link S3DeleteFailedException} 발생</li>
 * </ul>
 * 이 서비스는 {@link S3AsyncClient}를 통해 AWS SDK v2 기반 비동기 I/O 처리로 수행됩니다.
 * @author snowykte0426
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileDeleteService implements FileDeleteUseCase {

    private final S3AsyncClient s3AsyncClient;
    private final AwsEnvironment awsEnvironment;

    @Override
    @Async
    public CompletableFuture<Void> execute(String uniqueFileName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(awsEnvironment.bucket().name())
                .key(uniqueFileName)
                .build();
        return s3AsyncClient.deleteObject(deleteObjectRequest).thenAccept(response -> log.info("File deleted successfully: {}", uniqueFileName)
        ).exceptionally(throwable -> {
            log.error("Failed to delete file: {}", uniqueFileName, throwable);
            throw new S3DeleteFailedException();
        });
    }
}