package team.incube.gsmc.v2.domain.evidence.application.usecase;

import org.springframework.web.multipart.MultipartFile;
import team.incube.gsmc.v2.domain.evidence.domain.constant.EvidenceType;

/**
 * 현재 사용자의 활동 증빙자료를 수정하는 유스케이스 인터페이스입니다.
 * <p>제목, 내용, 파일 또는 이미지 URL, 증빙자료 타입 등을 수정할 수 있으며,
 * 주어진 증빙자료 ID에 해당하는 항목을 갱신합니다.
 * <p>파일 또는 이미지 URL 중 하나 이상이 필수이며, 기존 파일이 삭제되고 새 항목으로 교체됩니다.
 * @author suuuuuuminnnnnn
 */
public interface UpdateActivityEvidenceByCurrentUserUseCase {
    void execute(Long evidenceId, String title, String content, MultipartFile file, EvidenceType evidenceType);
}
