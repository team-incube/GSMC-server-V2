package team.incude.gsmc.v2.global.thirdparty.aws.s3.usecase.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import team.incude.gsmc.v2.global.thirdparty.aws.exception.S3DeleteFailedException;
import team.incude.gsmc.v2.global.thirdparty.aws.s3.usecase.FileDeleteUseCase;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileDeleteService implements FileDeleteUseCase {

    private final S3AsyncClient s3AsyncClient;
    @Value("${aws.bucket.name}")
    private String bucketName;

    @Override
    @Async
    public CompletableFuture<Void> execute(String uniqueFileName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(uniqueFileName)
                .build();
        return s3AsyncClient.deleteObject(deleteObjectRequest).thenAccept(response -> {
            log.info("File deleted successfully: {}", uniqueFileName);
        }).exceptionally(throwable -> {
            log.error("Failed to delete file: {}", uniqueFileName, throwable);
            throw new S3DeleteFailedException();
        });
    }
}