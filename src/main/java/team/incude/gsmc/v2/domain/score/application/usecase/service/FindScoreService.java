package team.incude.gsmc.v2.domain.score.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.application.usecase.FindScoreUseCase;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.domain.score.presentation.data.GetScoreDto;
import team.incude.gsmc.v2.domain.score.presentation.data.response.GetScoreResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindScoreService implements FindScoreUseCase {

    private final ScorePersistencePort scorePersistencePort;

    @Override
    public GetScoreResponse execute() {
        return findScore("");
    }

    @Override
    public GetScoreResponse execute(String email) {
        return findScore(email);
    }

    private GetScoreResponse findScore(String email) {
        List<Score> scores = scorePersistencePort.findScoreByMemberEmail(email);
        return new GetScoreResponse(
                -1,
                scores.stream()
                        .map(score -> new GetScoreDto(
                                score.getCategory().getName(),
                                score.getValue()
                        )).toList()
        );
    }
}