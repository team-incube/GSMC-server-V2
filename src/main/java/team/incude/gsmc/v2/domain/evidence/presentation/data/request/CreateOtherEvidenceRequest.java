package team.incude.gsmc.v2.domain.evidence.presentation.data.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.global.annotation.validator.NotEmptyFile;

/**
 * 기타 증빙자료 생성을 위한 요청 DTO입니다.
 * <p>카테고리 이름과 파일을 포함하여 기타 유형의 증빙자료를 생성할 때 사용됩니다.
 * @param categoryName 증빙자료 카테고리 이름
 * @param file 첨부할 증빙자료 파일 (비어 있지 않아야 함)
 * @author suuuuuuminnnnnn, snowykte0426
 */

public record CreateOtherEvidenceRequest(
        @NotNull String categoryName,
        @NotEmptyFile MultipartFile file
) {
}
