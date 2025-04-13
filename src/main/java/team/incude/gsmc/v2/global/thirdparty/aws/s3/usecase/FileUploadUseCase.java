package team.incude.gsmc.v2.global.thirdparty.aws.s3.usecase;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

public interface FileUploadUseCase {
    CompletableFuture<String> execute(String fileName, InputStream fileInputStream);
}