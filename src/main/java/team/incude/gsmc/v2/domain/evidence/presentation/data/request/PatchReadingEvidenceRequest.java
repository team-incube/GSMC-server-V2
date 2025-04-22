package team.incude.gsmc.v2.domain.evidence.presentation.data.request;

public record PatchReadingEvidenceRequest(
        String title,
        String author,
        String content,
        Integer page
) {
}
