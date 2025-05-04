package team.incude.gsmc.v2.domain.evidence.application.port;


import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

/**
 * AWS S3와의 파일 업로드 및 삭제 기능을 정의하는 포트 인터페이스입니다.
 * <p>도메인 계층에서 S3에 의존하지 않고 파일 저장 및 삭제 작업을 추상화하여 사용할 수 있도록 설계되었습니다.
 * <p>비동기 방식으로 동작하며, {@link java.util.concurrent.CompletableFuture}를 통해 결과를 반환합니다.
 * 주요 기능:
 * <ul>
 *   <li>{@code uploadFile(String fileName, InputStream fileInputStream)} - 파일 업로드</li>
 *   <li>{@code deleteFile(String fileName)} - 파일 삭제</li>
 * </ul>
 * 이 포트는 실제 구현체(S3Adapter 등)를 통해 외부 시스템과 통신합니다.
 * @author snowykte0426
 */
public interface S3Port {
    CompletableFuture<String> uploadFile(String fileName, InputStream fileInputStream);

    CompletableFuture<Void> deleteFile(String fileName);
}