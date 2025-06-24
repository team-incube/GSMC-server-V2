package team.incube.gsmc.v2.domain.evidence.application.usecase;

import org.springframework.web.multipart.MultipartFile;
import team.incube.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incube.gsmc.v2.domain.evidence.presentation.data.response.CreateDraftEvidenceResponse;

import java.util.UUID;

/**
 * 활동 증빙자료 임시저장을 위한 유스케이스 인터페이스입니다.
 * <p>제목, 내용, 카테고리, 이미지 또는 파일을 기반으로 활동 증빙자료를 임시저장합니다.
 * <p>{@code UUID id}가 존재할 경우 기존 임시저장을 수정하며, 없을 경우 새 임시저장을 생성합니다.
 * <p>이 유스케이스는 정식 저장 전에 작성 중인 데이터를 임시로 저장하고,
 * 이후 계속해서 편집하거나 등록할 수 있도록 지원합니다.
 * @author suuuuuuminnnnnn
 */
public interface CreateDraftActivityEvidenceUseCase {
    CreateDraftEvidenceResponse execute(UUID id, String categoryName, String title, String content, MultipartFile file, String imageUrl, EvidenceType activityType);
}
