package team.incude.gsmc.v2.global.thirdparty.aws.s3;

import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;
import team.incude.gsmc.v2.global.thirdparty.aws.s3.usecase.FileDeleteUseCase;
import team.incude.gsmc.v2.global.thirdparty.aws.s3.usecase.FileUploadUseCase;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class S3Adapter implements S3Port {

    private final FileUploadUseCase fileUploadUseCase;
    private final FileDeleteUseCase fileDeleteUseCase;

    @Override
    public CompletableFuture<String> uploadFile(String fileName, InputStream fileInputStream) {
        return fileUploadUseCase.execute(fileName, fileInputStream);
    }

    @Override
    public CompletableFuture<Void> deleteFile(String fileName) {
        return fileDeleteUseCase.execute(fileName);
    }
}