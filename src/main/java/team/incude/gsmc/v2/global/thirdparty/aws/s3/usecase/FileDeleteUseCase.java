package team.incude.gsmc.v2.global.thirdparty.aws.s3.usecase;

import java.util.concurrent.CompletableFuture;

public interface FileDeleteUseCase {
    CompletableFuture<Void> execute(String fileName);
}