package team.incude.gsmc.v2.domain.evidence.presentation.data.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.global.annotation.validator.NotEmptyFile;

/**
 * 점수 기반 기타 증빙자료 수정을 위한 요청 DTO입니다.
 * <p>점수 값, 파일, 이미지 URL을 수정할 수 있으며, 기존 증빙자료의 내용을 갱신할 때 사용됩니다.
 * <p>세 필드는 모두 선택적으로 입력 가능하며, 변경이 필요한 항목만 전달하면 됩니다.
 * @param value 새로운 점수 값 (선택)
 * @param file 새로 첨부할 증빙자료 파일
 * @author suuuuuuminnnnnn
 */
public record PatchOtherScoringEvidenceRequest(
        @NotNull Integer value,
        @NotEmptyFile MultipartFile file
) {
}
