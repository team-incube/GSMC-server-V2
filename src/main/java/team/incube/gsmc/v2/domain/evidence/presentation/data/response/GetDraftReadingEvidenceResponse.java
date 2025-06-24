package team.incube.gsmc.v2.domain.evidence.presentation.data.response;

import java.util.UUID;

/**
 * 독서 증빙자료 임시저장 조회 응답 DTO입니다.
 * <p>임시저장된 독서 증빙자료의 제목, 저자, 페이지 수, 독서 내용을 클라이언트에 제공합니다.
 * 클라이언트는 이를 기반으로 임시저장 편집 화면을 구성할 수 있습니다.
 * @param draftId 임시저장 ID
 * @param title 책 제목
 * @param author 저자명
 * @param page 페이지 수
 * @param content 독서 내용 요약
 * @author suuuuuuminnnnnn
 */
public record GetDraftReadingEvidenceResponse(
        UUID draftId,
        String title,
        String author,
        Integer page,
        String content
) {
}
