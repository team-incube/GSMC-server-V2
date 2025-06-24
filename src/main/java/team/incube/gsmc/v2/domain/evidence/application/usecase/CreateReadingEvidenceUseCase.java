package team.incube.gsmc.v2.domain.evidence.application.usecase;

import java.util.UUID;

/**
 * 독서 증빙자료 생성을 위한 유스케이스 인터페이스입니다.
 * <p>책 제목, 저자, 페이지 수, 독서 내용, 임시저장 ID를 기반으로 독서 증빙자료를 정식 등록합니다.
 * <p>{@code draftId}가 존재할 경우, 기존 임시저장 데이터를 활용하여 등록되며, 이후 임시저장은 삭제됩니다.
 * 이 유스케이스는 {@code EvidenceApplicationAdapter}를 통해 호출되며,
 * 도메인 서비스에서 구현됩니다.
 * @author suuuuuuminnnnnn
 */
public interface CreateReadingEvidenceUseCase {
    void execute(String title, String author, int page, String content, UUID draftId);
}
