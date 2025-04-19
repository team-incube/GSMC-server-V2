package team.incude.gsmc.v2.domain.evidence.application.usecase;

public interface CreateReadingEvidenceUseCase {
    void execute(String title, String author, int page, String content);
}
