package team.incude.gsmc.v2.domain.evidence.presentation.data.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;

import java.util.UUID;

/**
 * 활동 증빙자료 생성을 위한 요청 DTO입니다.
 * <p>활동 유형, 제목, 내용, 선택적으로 파일 혹은 이미지 URL을 포함하여 증빙자료를 등록합니다.
 * <p>{@code draftId}가 제공되면 임시저장에서 불러온 후 저장 처리로 간주됩니다.
 * @param categoryName 활동 카테고리 이름
 * @param title 증빙자료 제목 (최대 100자)
 * @param content 증빙자료 내용 (최대 1500자)
 * @param file 첨부 파일 (선택)
 * @param imageUrl 외부 이미지 URL (선택)
 * @param activityType 활동 유형
 * @param draftId 임시저장 식별자 (선택)
 * @author suuuuuuminnnnnn
 */
public record CreateActivityEvidenceRequest(
        @NotNull String categoryName,
        @NotNull @Size(max = 100) String title,
        @NotNull @Size(max = 1500) String content,
        MultipartFile file,
        String imageUrl,
        @NotNull EvidenceType activityType,
        UUID draftId
) {
}
