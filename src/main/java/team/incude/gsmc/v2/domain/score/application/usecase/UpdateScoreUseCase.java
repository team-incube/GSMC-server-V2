package team.incude.gsmc.v2.domain.score.application.usecase;

public interface UpdateScoreUseCase {
    void execute(String categoryName, Integer value);

    void execute(String email, String categoryName, Integer value);
}