package team.incube.gsmc.v2.domain.score.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incube.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incube.gsmc.v2.domain.score.application.usecase.GetStudentPercentInClassUseCase;
import team.incube.gsmc.v2.domain.score.domain.constant.ScoreOrder;
import team.incube.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;

@Service
@RequiredArgsConstructor
public class GetStudentPercentInClassService implements GetStudentPercentInClassUseCase {

    private final ScorePersistencePort scorePersistencePort;
    private final CurrentMemberProvider currentMemberProvider;

    public Integer execute(ScoreOrder scoreOrder, Integer grade, Integer classNumber) {

        return switch(scoreOrder) {
            case HIGH -> scorePersistencePort.getStudentHighPercentileByEmailInClass(currentMemberProvider.getCurrentUser().getEmail(), grade, classNumber);
            case LOW -> scorePersistencePort.getStudentLowPercentileByEmailInClass(currentMemberProvider.getCurrentUser().getEmail(), grade, classNumber);
        };
    }
}
