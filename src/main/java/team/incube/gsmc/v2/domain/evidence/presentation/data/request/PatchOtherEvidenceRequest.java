package team.incube.gsmc.v2.domain.evidence.presentation.data.request;

import org.springframework.web.multipart.MultipartFile;
import team.incube.gsmc.v2.global.annotation.validator.NotEmptyFile;

/**
 * 기타 증빙자료 수정을 위한 요청 DTO입니다.
 * <p>파일 또는 이미지 URL을 새로 지정하여 기존 기타 증빙자료를 수정할 때 사용됩니다.
 * <p>두 필드는 선택적이며, 둘 중 하나 이상이 존재해야 합니다.
 * @param file 새로 첨부할 증빙자료 파일
 * @author suuuuuuminnnnnn
 */

public record PatchOtherEvidenceRequest(
        @NotEmptyFile MultipartFile file
) {
}
