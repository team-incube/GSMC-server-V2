package team.incude.gsmc.v2.domain.evidence.application.port;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

public interface S3Port {
    CompletableFuture<String> uploadFile(String fileName, InputStream fileInputStream);

    CompletableFuture<Void> deleteFile(String fileName);
}