package team.incude.gsmc.v2.domain.score.application.port;

import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.util.List;
import java.util.Optional;

@Port(direction = PortDirection.OUTBOUND)
public interface ScorePersistencePort {
    Score findScoreByCategoryNameAndMemberEmail(String name, String email);

    Score findScoreByCategoryNameAndMemberEmailWithLock(String name, String email);

    List<Score> findScoreByMemberEmail(String email);

    Score saveScore(Score score);
}