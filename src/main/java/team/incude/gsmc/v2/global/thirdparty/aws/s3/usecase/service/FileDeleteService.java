package team.incude.gsmc.v2.global.thirdparty.aws.s3.usecase.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.global.thirdparty.aws.exception.S3DeleteFailedException;
import team.incude.gsmc.v2.global.thirdparty.aws.s3.usecase.FileDeleteUseCase;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class FileDeleteService implements FileDeleteUseCase {

    private final AmazonS3 s3Client;
    @Value("${aws.bucket.name}")
    private String bucketName;

    @Override
    public CompletableFuture<Void> execute(String fileName) {
        try {
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            throw new S3DeleteFailedException();
        }
    }
}