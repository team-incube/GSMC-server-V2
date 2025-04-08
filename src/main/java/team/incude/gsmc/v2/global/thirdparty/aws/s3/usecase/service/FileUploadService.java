package team.incude.gsmc.v2.global.thirdparty.aws.s3.usecase.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.global.thirdparty.aws.exception.S3UploadFailedException;
import team.incude.gsmc.v2.global.thirdparty.aws.s3.usecase.FileUploadUseCase;

import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class FileUploadService implements FileUploadUseCase {

    private final AmazonS3 s3Client;
    @Value("${aws.bucket.name}")
    private String bucketName;

    @Override
    public CompletableFuture<String> execute(String fileName, InputStream fileInputStream) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            String uniqueFileName = UUID.randomUUID() + "/" + fileName;
            s3Client.putObject(bucketName, uniqueFileName, fileInputStream, metadata);
            return CompletableFuture.completedFuture(String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, "ap-northeast-2", uniqueFileName));
        } catch (Exception e) {
            throw new S3UploadFailedException();
        }
    }
}