package team.incube.gsmc.v2.domain.evidence.application.usecase;

import org.springframework.web.multipart.MultipartFile;
import team.incube.gsmc.v2.domain.evidence.domain.constant.EvidenceType;

import java.util.UUID;


/**
 * 활동 증빙자료 생성을 위한 유스케이스 인터페이스입니다.
 * <p>활동 유형, 카테고리, 제목, 내용, 파일 또는 이미지 URL을 포함하여 증빙자료를 생성합니다.
 * <p>{@code draftId}가 존재할 경우, 임시저장에서 불러온 후 정식 저장으로 처리됩니다.
 * 이 인터페이스는 {@code EvidenceApplicationAdapter}를 통해 호출되며, 실제 구현체는 도메인 서비스에서 구성됩니다.
 * @author suuuuuuminnnnnn
 */
public interface CreateActivityEvidenceUseCase {
    void execute(String categoryName, String title, String content, MultipartFile file, String imageUrl, EvidenceType activityType, UUID draftId);
}
