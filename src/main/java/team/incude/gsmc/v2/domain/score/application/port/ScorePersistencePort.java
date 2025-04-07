package team.incude.gsmc.v2.domain.score.application.port;

import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.util.Optional;

@Port(direction = PortDirection.OUTBOUND)
public interface ScorePersistencePort {
    Optional<Score> findScoreByNameAndEmail(String name, String email);

    void saveScore(Score score);
}