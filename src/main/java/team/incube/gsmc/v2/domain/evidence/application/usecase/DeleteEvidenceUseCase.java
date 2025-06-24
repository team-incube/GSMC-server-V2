package team.incube.gsmc.v2.domain.evidence.application.usecase;

/**
 * 증빙자료를 삭제하기 위한 유스케이스 인터페이스입니다.
 * <p>증빙자료 ID를 기준으로 활동, 독서, 기타 유형에 관계없이 해당 증빙자료를 삭제합니다.
 * 삭제 요청은 관리자 또는 해당 자료의 작성자에 의해 수행될 수 있습니다.
 * 이 인터페이스는 {@code EvidenceApplicationAdapter}에서 호출되어 도메인 서비스에서 구현됩니다.
 * @author suuuuuuminnnnnn
 */
public interface DeleteEvidenceUseCase {
    void execute(Long evidenceId);
}
