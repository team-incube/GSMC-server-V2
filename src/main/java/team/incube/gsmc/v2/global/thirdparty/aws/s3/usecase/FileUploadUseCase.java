package team.incube.gsmc.v2.global.thirdparty.aws.s3.usecase;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;


/**
 * S3에 파일을 업로드하는 유스케이스를 정의하는 인터페이스입니다.
 * <p>파일 이름과 입력 스트림을 받아 S3에 비동기 방식으로 파일을 업로드하고,
 * 업로드된 파일의 URI를 {@link CompletableFuture}를 통해 반환합니다.
 * 이 인터페이스는 어댑터 계층(S3Adapter 등)에서 구현되며,
 * 도메인 계층이 파일 업로드 세부 구현에 의존하지 않도록 추상화합니다.
 * @author snowykte0426
 */
public interface FileUploadUseCase {
    CompletableFuture<String> execute(String fileName, InputStream fileInputStream);
}