package team.incude.gsmc.v2.global.thirdparty.aws.s3.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import team.incude.gsmc.v2.global.thirdparty.aws.s3.usecase.FileDeleteUseCase;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class FileDeleteService implements FileDeleteUseCase {

    private final S3AsyncClient s3AsyncClient;
    @Value("${aws.bucket.name}")
    private String bucketName;

    @Override
    public CompletableFuture<Void> execute(String uniqueFileName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(uniqueFileName)
                .build();
        return s3AsyncClient.deleteObject(deleteObjectRequest).thenAccept(response -> {
            System.out.println("File deleted successfully: " + uniqueFileName);
        });
    }
}