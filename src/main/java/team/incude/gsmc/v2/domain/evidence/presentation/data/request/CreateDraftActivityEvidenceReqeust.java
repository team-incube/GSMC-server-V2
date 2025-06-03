package team.incude.gsmc.v2.domain.evidence.presentation.data.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;

import java.util.UUID;

/**
 * 활동 증빙자료 임시저장을 위한 요청 DTO입니다.
 * <p>활동 카테고리, 제목, 내용, 파일 또는 이미지 URL을 포함하여
 * 임시로 저장할 활동 증빙자료 정보를 전달합니다.
 * <p>이 요청은 실제 증빙자료로 확정되기 전 임시저장 상태로 관리되며,
 * 클라이언트가 추후 이어서 작성할 수 있도록 설계되었습니다.
 * @param draftId 기존 임시저장 ID (수정 시 사용, 생성 시 null 가능)
 * @param categoryName 활동 카테고리명
 * @param title 증빙자료 제목
 * @param content 증빙자료 내용
 * @param file 첨부할 증빙자료 파일 (선택)
 * @param imageUrl 외부 이미지 URL (선택)
 * @param activityType 활동 유형
 * @author suuuuuuminnnnnn
 */
public record CreateDraftActivityEvidenceReqeust(
        UUID draftId,
        String categoryName,
        String title,
        String content,
        MultipartFile file,
        String imageUrl,
        @NotNull EvidenceType activityType
) {
}
