package team.incube.gsmc.v2.domain.evidence.application.usecase;

import org.springframework.web.multipart.MultipartFile;

/**
 * 기타 증빙자료 생성을 위한 유스케이스 인터페이스입니다.
 * <p>카테고리 이름과 파일을 기반으로 기타 유형의 증빙자료를 생성합니다.
 * <p>정식 증빙자료로 저장되며, 파일은 필수이며 비어 있으면 안 됩니다.
 * 이 유스케이스는 {@code EvidenceApplicationAdapter}를 통해 호출되며,
 * 도메인 서비스에서 구현됩니다.
 * @author suuuuuuminnnnnn
 */
public interface CreateOtherEvidenceUseCase {
    void execute(String categoryName, MultipartFile file);
}
