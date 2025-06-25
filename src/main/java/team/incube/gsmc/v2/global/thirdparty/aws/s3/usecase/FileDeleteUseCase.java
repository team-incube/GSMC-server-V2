package team.incube.gsmc.v2.global.thirdparty.aws.s3.usecase;

import java.util.concurrent.CompletableFuture;

/**
 * S3에서 파일을 삭제하는 유스케이스를 정의하는 인터페이스입니다.
 * <p>파일 이름을 입력받아 해당 파일을 S3 버킷에서 비동기 방식으로 삭제합니다.
 * 삭제 작업의 결과는 {@link CompletableFuture}를 통해 비동기적으로 처리됩니다.
 * 이 유스케이스는 어댑터 계층(S3Adapter 등)에서 구현되며,
 * 도메인 계층이 외부 저장소(S3)에 직접 의존하지 않도록 추상화를 제공합니다.
 * @author snowykte0426
 */
public interface FileDeleteUseCase {
    CompletableFuture<Void> execute(String fileName);
}