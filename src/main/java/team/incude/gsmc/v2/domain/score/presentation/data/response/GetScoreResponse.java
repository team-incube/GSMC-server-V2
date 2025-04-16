package team.incude.gsmc.v2.domain.score.presentation.data.response;

import team.incude.gsmc.v2.domain.score.presentation.data.GetScoreDto;

import java.util.List;

public record GetScoreResponse(
        Integer totalScore,
        List<GetScoreDto> scores
) {
}