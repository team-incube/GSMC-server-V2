package team.incude.gsmc.v2.domain.evidence.application.usecase;

import org.springframework.web.multipart.MultipartFile;

/**
 * 현재 사용자의 기타 증빙자료를 수정하는 유스케이스 인터페이스입니다.
 * <p>기타 증빙자료의 파일 또는 이미지 URL을 새롭게 교체합니다.
 * <p>파일 또는 이미지 URL은 둘 중 하나 이상 존재해야 하며, 기존 첨부 내용은 제거됩니다.
 * @author suuuuuuminnnnnn
 */
public interface UpdateOtherEvidenceByCurrentUserUseCase {
    void execute(Long evidenceId, MultipartFile file);
}
