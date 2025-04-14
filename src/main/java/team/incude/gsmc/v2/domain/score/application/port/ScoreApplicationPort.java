package team.incude.gsmc.v2.domain.score.application.port;

import team.incude.gsmc.v2.domain.score.presentation.data.response.GetScoreResponse;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

@Port(direction = PortDirection.INBOUND)
public interface ScoreApplicationPort {
    GetScoreResponse findCurrentScore();

    GetScoreResponse findScoreByEmail(String email);

    void updateCurrentScore(String categoryName, Integer value);

    void updateScoreByEmail(String email, String categoryName, Integer value);

    // void simulateScore(GetScoreSimulateRequest request);
}