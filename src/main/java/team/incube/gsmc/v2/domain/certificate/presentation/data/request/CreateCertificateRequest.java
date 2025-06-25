package team.incube.gsmc.v2.domain.certificate.presentation.data.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;
import team.incube.gsmc.v2.global.annotation.validator.NotEmptyFile;

import java.time.LocalDate;

/**
 * 자격증 생성을 위한 요청 데이터를 담는 DTO입니다. 클라이언트가 자격증을 등록할 때 사용하는 요청 형식으로, 다음 필드들을 포함합니다:
 * <ul>
 *   <li>{@code name} - 자격증 이름 (필수)</li>
 *   <li>{@code acquisitionDate} - 자격증 취득일 (필수)</li>
 *   <li>{@code file} - 자격증 이미지 또는 PDF 파일 (필수)</li>
 * </ul>
 * 이 DTO는 {@link NotNull} 및 {@link NotEmptyFile} 어노테이션을 사용하여 유효성을 검사합니다.
 * @author snowykte0426
 */
public record CreateCertificateRequest(
        @NotNull String name,
        @NotNull LocalDate acquisitionDate,
        @NotEmptyFile MultipartFile file
) {
}