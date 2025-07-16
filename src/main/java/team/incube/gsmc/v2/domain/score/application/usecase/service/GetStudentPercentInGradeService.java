package team.incube.gsmc.v2.domain.score.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incube.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incube.gsmc.v2.domain.score.application.usecase.GetStudentPercentInGradeUseCase;
import team.incube.gsmc.v2.domain.score.domain.constant.ScoreOrder;
import team.incube.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;

@Service
@RequiredArgsConstructor
public class GetStudentPercentInGradeService implements GetStudentPercentInGradeUseCase {

    private final ScorePersistencePort scorePersistencePort;
    private final CurrentMemberProvider currentMemberProvider;

    public Integer execute(ScoreOrder scoreOrder, Integer grade) {

        return switch(scoreOrder) {
            case HIGH -> scorePersistencePort.getStudentHighPercentileByEmailInGrade(currentMemberProvider.getCurrentUser().getEmail(), grade);
            case LOW -> scorePersistencePort.getStudentLowPercentileByEmailInGrade(currentMemberProvider.getCurrentUser().getEmail(), grade);
        };
    }
}
