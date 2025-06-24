package team.incube.gsmc.v2.domain.evidence.application.usecase;

/**
 * 현재 사용자의 독서 증빙자료를 수정하는 유스케이스 인터페이스입니다.
 * <p>책 제목, 저자, 읽은 페이지 수, 독서 내용을 수정할 수 있으며,
 * 해당 증빙자료 ID를 기준으로 기존 정보를 갱신합니다.
 * 이 유스케이스는 사용자가 등록한 독서 증빙자료의 내용을 편집할 수 있도록 합니다.
 * @author suuuuuuminnnnnn
 */
public interface UpdateReadingEvidenceByCurrentUserUseCase {
    void execute(Long evidenceId, String title, String author, String content, int page);
}
