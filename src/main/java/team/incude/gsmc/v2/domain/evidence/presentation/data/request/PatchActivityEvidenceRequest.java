package team.incude.gsmc.v2.domain.evidence.presentation.data.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

/**
 * 활동 증빙자료 수정을 위한 요청 DTO입니다.
 * <p>제목, 내용, 파일 또는 이미지 URL을 수정할 수 있으며, 기존 증빙자료에 대한 내용을 변경할 때 사용됩니다.
 * @param title 수정할 제목 (최대 100자)
 * @param content 수정할 내용 (최대 1500자)
 * @param file 새로 업로드할 파일
 * @author suuuuuuminnnnnn
 */
public record PatchActivityEvidenceRequest(
        @Size(max = 100) String title,
        @Size(max = 1500) String content,
        MultipartFile file
) {
}
