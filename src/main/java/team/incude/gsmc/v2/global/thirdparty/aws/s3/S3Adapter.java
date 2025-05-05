package team.incude.gsmc.v2.global.thirdparty.aws.s3;

import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;
import team.incude.gsmc.v2.global.thirdparty.aws.s3.usecase.FileDeleteUseCase;
import team.incude.gsmc.v2.global.thirdparty.aws.s3.usecase.FileUploadUseCase;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

/**
 * AWS S3 파일 연동을 위한 어댑터 클래스입니다.
 *
 * <p>{@link S3Port}의 구현체로, 파일 업로드 및 삭제 기능을 실제로 수행합니다.
 * 내부적으로 {@link FileUploadUseCase}, {@link FileDeleteUseCase}를 통해 작업을 위임하며,
 * 비동기 방식으로 동작하여 {@link CompletableFuture} 형태로 결과를 반환합니다.
 * <p>{@code @Adapter(direction = PortDirection.OUTBOUND)}로 선언되어 도메인 계층이 외부 시스템에 의존하지 않도록 구성됩니다.
 * <p>주요 역할:
 * <ul>
 *   <li>파일 업로드 요청 위임 및 결과 반환</li>
 *   <li>파일 삭제 요청 위임 및 처리</li>
 * </ul>
 * @author snowykte0426
 */
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