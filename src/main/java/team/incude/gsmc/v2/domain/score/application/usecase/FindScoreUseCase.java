package team.incude.gsmc.v2.domain.score.application.usecase;

import team.incude.gsmc.v2.domain.score.presentation.data.response.GetScoreResponse;

public interface FindScoreUseCase {
    GetScoreResponse execute();

    GetScoreResponse execute(String email);
}