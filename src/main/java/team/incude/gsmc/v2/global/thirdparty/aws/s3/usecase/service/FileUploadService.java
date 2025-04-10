package team.incude.gsmc.v2.global.thirdparty.aws.s3.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

@Service
@RequiredArgsConstructor
public class FileUploadService implements FileUploadUseCase {

    private final S3AsyncClient s3AsyncClient;
    @Value("${aws.bucket.name}")
    private String bucketName;
    @Value("${aws.bucket.region}")
    private String region;

    @Override
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