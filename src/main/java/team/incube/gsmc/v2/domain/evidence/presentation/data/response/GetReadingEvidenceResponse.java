package team.incube.gsmc.v2.domain.evidence.presentation.data.response;

import team.incube.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;

/**
 * 독서 증빙자료 단건 조회 응답 DTO입니다.
 * <p>증빙자료 ID, 책 제목, 저자, 페이지 수, 내용, 검토 상태를 포함하여 클라이언트에 전달합니다.
 * 클라이언트는 이 정보를 바탕으로 독서 증빙자료 상세 화면을 구성할 수 있습니다.
 * @param id 증빙자료 ID
 * @param title 책 제목
 * @param author 저자명
 * @param page 페이지 수
 * @param content 독서 내용 요약
 * @param status 검토 상태 (PENDING, APPROVED, REJECTED 등)
 * @author suuuuuuminnnnnn
 */
public record GetReadingEvidenceResponse(
        Long id,
        String title,
        String author,
        Integer page,
        String content,
        ReviewStatus status
) {
}
