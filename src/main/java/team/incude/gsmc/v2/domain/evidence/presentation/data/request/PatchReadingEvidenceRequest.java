package team.incude.gsmc.v2.domain.evidence.presentation.data.request;

import jakarta.validation.constraints.Size;

/**
 * 독서 증빙자료 수정을 위한 요청 DTO입니다.
 * <p>책 제목, 저자, 읽은 페이지 수, 읽은 내용을 수정할 수 있으며,
 * 기존에 저장된 독서 증빙자료 정보를 갱신할 때 사용됩니다.
 * @param title 책 제목 (최대 100자)
 * @param author 저자명 (최대 100자)
 * @param content 독서 내용 요약 (최대 1500자)
 * @param page 페이지 수
 * @author suuuuuuminnnnnn
 */

public record PatchReadingEvidenceRequest(
        @Size(max = 100) String title,
        @Size(max = 100) String author,
        @Size(max = 1500) String content,
        Integer page
) {
}
