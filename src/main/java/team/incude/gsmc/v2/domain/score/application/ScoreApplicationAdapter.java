package team.incude.gsmc.v2.domain.score.application;

import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.score.application.port.ScoreApplicationPort;
import team.incude.gsmc.v2.domain.score.application.usecase.FindScoreUseCase;
import team.incude.gsmc.v2.domain.score.presentation.data.response.GetScoreResponse;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

@Adapter(direction = PortDirection.INBOUND)
@RequiredArgsConstructor
public class ScoreApplicationAdapter implements ScoreApplicationPort {

    private final FindScoreUseCase findScoreUseCase;

    @Override
    public GetScoreResponse findCurrentScore() {
        return findScoreUseCase.execute();
    }

    @Override
    public GetScoreResponse findScoreByEmail(String email) {
        return findScoreUseCase.execute(email);
    }

    @Override
    public void updateCurrentScore(String categoryName, Integer value) {

    }

    @Override
    public void updateScoreByEmail(String email, String categoryName, Integer value) {

    }
}