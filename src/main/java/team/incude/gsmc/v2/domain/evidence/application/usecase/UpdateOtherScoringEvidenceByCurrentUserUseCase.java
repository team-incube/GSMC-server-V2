package team.incude.gsmc.v2.domain.evidence.application.usecase;

import org.springframework.web.multipart.MultipartFile;

/**
 * 현재 사용자의 점수 기반 기타 증빙자료를 수정하는 유스케이스 인터페이스입니다.
 * <p>점수 값, 파일, 이미지 URL을 변경할 수 있으며,
 * 기존 증빙자료의 첨부 파일 및 점수를 새로운 값으로 대체합니다.
 * <p>파일 또는 이미지 URL은 최소한 하나 이상이 존재해야 하며,
 * 수정 시 기존 파일은 삭제 처리됩니다.
 * @author suuuuuuminnnnnn
 */
public interface UpdateOtherScoringEvidenceByCurrentUserUseCase {
    void execute(Long evidenceId, MultipartFile file, int value);
}
