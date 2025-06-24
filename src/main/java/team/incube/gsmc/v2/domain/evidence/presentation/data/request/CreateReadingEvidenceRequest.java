package team.incube.gsmc.v2.domain.evidence.presentation.data.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;


/**
 * 독서 증빙자료 생성을 위한 요청 DTO입니다.
 * <p>책 제목, 저자, 페이지 수, 독서 내용을 포함하여 증빙자료로 저장할 때 사용됩니다.
 * {@code draftId}가 주어질 경우, 기존 임시저장 데이터를 기반으로 저장됩니다.
 * @param title 책 제목 (최대 100자)
 * @param author 저자명 (최대 100자)
 * @param page 책 페이지 수
 * @param content 독서 내용 요약 (최대 1500자)
 * @param draftId 임시저장 식별자 (선택)
 * @author suuuuuuminnnnnn
 */
public record CreateReadingEvidenceRequest(
        @NotNull @Size(max = 100) String title,
        @NotNull @Size(max = 100) String author,
        @NotNull Integer page,
        @NotNull @Size(max = 1500) String content,
        UUID draftId
) {
}
