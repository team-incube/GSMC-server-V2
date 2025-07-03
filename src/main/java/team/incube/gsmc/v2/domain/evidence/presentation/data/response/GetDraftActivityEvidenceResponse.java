package team.incube.gsmc.v2.domain.evidence.presentation.data.response;

import team.incube.gsmc.v2.domain.evidence.domain.constant.EvidenceType;

import java.util.UUID;

/**
 * 활동 증빙자료 임시저장 조회 응답 DTO입니다.
 * <p>임시저장된 활동 증빙자료의 제목, 내용, 이미지 URL, 카테고리명, 활동 유형 등의 정보를 제공합니다.
 * 클라이언트는 이 응답을 바탕으로 임시저장 편집 화면을 구성할 수 있습니다.
 * @param draftId 임시저장 ID
 * @param categoryName 활동 카테고리명
 * @param title 활동 제목
 * @param content 활동 내용
 * @param imageUri 첨부된 이미지 URL
 * @param activityType 활동 유형 (EXTERNAL, INTERNAL 등)
 * @author suuuuuuminnnnnn
 */
public record GetDraftActivityEvidenceResponse(
        UUID draftId,
        String categoryName,
        String title,
        String content,
        String imageUri,
        EvidenceType activityType
) {
}
