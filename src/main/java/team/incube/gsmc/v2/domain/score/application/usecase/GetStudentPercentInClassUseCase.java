package team.incube.gsmc.v2.domain.score.application.usecase;

import team.incube.gsmc.v2.domain.score.domain.constant.ScoreOrder;

public interface GetStudentPercentInClassUseCase {
    Integer execute(ScoreOrder scoreOrder, Integer grade, Integer classNumber);
}
