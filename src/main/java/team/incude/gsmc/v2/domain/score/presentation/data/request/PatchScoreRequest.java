package team.incude.gsmc.v2.domain.score.presentation.data.request;

public record PatchScoreRequest(
        String categoryName,
        Integer value
) {
}