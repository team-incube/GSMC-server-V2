package team.incude.gsmc.v2.domain.score.application.usecase.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incude.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.application.usecase.FindScoreUseCase;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.domain.score.presentation.data.GetScoreDto;
import team.incude.gsmc.v2.domain.score.presentation.data.response.GetScoreResponse;
import team.incude.gsmc.v2.global.security.jwt.usecase.service.CurrentMemberProvider;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindScoreService implements FindScoreUseCase {

    private final ScorePersistencePort scorePersistencePort;
    private final StudentDetailPersistencePort studentDetailPersistencePort;
    private final CurrentMemberProvider currentMemberProvider;

    @Override
    public GetScoreResponse execute() {
        return findScore(currentMemberProvider.getCurrentUser().getEmail());
    }

    @Override
    public GetScoreResponse execute(String studentCode) {
        return findScore(studentCode);
    }

    private GetScoreResponse findScore(String studentCode) {
        List<Score> scores = scorePersistencePort.findScoreByStudentDetailStudentCode(studentCode);
        return new GetScoreResponse(
                studentDetailPersistencePort.findTotalScoreByStudentCode(studentCode),
                scores.stream()
                        .map(score -> new GetScoreDto(
                                score.getCategory().getName(),
                                score.getValue()
                        )).toList()
        );
    }
}