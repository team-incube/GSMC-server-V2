package team.incude.gsmc.v2.domain.evidence.presentation.data.response;

import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;

/**
 * 활동 증빙자료 단건 조회 응답 DTO입니다.
 * <p>증빙자료의 식별자, 제목, 내용, 이미지 URI, 검토 상태, 카테고리 정보를 포함하여 반환합니다.
 * 클라이언트는 이 정보를 기반으로 증빙자료 상세 화면을 구성할 수 있습니다.
 * @param id 증빙자료 ID
 * @param title 활동 제목
 * @param content 활동 내용
 * @param imageUri 첨부 이미지 URI (파일 또는 외부 이미지 링크)
 * @param status 검토 상태 (PENDING, APPROVED, REJECTED 등)
 * @param categoryName 카테고리 이름
 * @author suuuuuuminnnnnn
 */
public record GetActivityEvidenceResponse(
        Long id,
        String title,
        String content,
        String imageUri,
        ReviewStatus status,
        String categoryName
) {
}
