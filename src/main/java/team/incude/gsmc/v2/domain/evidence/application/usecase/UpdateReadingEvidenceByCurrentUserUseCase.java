package team.incude.gsmc.v2.domain.evidence.application.usecase;

public interface UpdateReadingEvidenceByCurrentUserUseCase {
    void execute(Long evidenceId, String title, String author, String content, int page);
}
