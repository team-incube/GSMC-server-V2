package team.incude.gsmc.v2.domain.certificate.persentation.data.request;

import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.annotations.NotNull;

import java.time.LocalDate;

/**
 * 자격증 생성을 위한 요청 데이터를 담는 DTO입니다. 클라이언트가 자격증을 등록할 때 사용하는 요청 형식으로, 다음 필드들을 포함합니다:
 * <ul>
 *   <li>{@code name} - 자격증 이름 (필수)</li>
 *   <li>{@code acquisitionDate} - 자격증 취득일 (필수)</li>
 *   <li>{@code file} - 자격증 이미지 또는 PDF 파일 (필수)</li>
 * </ul>
 * 모든 필드는 {@code @NotNull}로 선언되어 있으며, 유효성 검증을 통해 필수 입력임을 보장합니다.
 *
 * @author snowykte0426
 */
public record CreateCertificateRequest(
        @NotNull String name,
        @NotNull LocalDate acquisitionDate,
        @NotNull MultipartFile file
) {
}