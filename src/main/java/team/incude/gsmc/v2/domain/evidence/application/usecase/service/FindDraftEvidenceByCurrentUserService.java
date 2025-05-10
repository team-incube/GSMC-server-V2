package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.evidence.application.port.DraftActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.DraftReadingEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.usecase.FindDraftEvidenceByCurrentUserUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.DraftActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.DraftReadingEvidence;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetDraftActivityEvidenceResponse;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetDraftEvidenceResponse;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetDraftReadingEvidenceResponse;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.global.security.jwt.usecase.service.CurrentMemberProvider;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindDraftEvidenceByCurrentUserService implements FindDraftEvidenceByCurrentUserUseCase {

    private final DraftActivityEvidencePersistencePort draftActivityEvidencePersistencePort;
    private final DraftReadingEvidencePersistencePort draftReadingEvidencePersistencePort;
    private final CurrentMemberProvider currentMemberProvider;

    @Override
    public GetDraftEvidenceResponse execute() {
        Member member = currentMemberProvider.getCurrentUser();

        List<DraftActivityEvidence> activityEvidences = draftActivityEvidencePersistencePort.findAllDraftActivityEvidenceByEmail(member.getEmail());
        List<DraftReadingEvidence> readingEvidences = draftReadingEvidencePersistencePort.findAllDraftReadingEvidenceByEmail(member.getEmail());

        List<GetDraftActivityEvidenceResponse> activityEvidenceResponses = createDraftActivityResponse(activityEvidences);
        List<GetDraftReadingEvidenceResponse> readingEvidenceResponses = createDraftReadingResponse(readingEvidences);

        return new GetDraftEvidenceResponse(activityEvidenceResponses, readingEvidenceResponses);
    }

    private List<GetDraftActivityEvidenceResponse> createDraftActivityResponse(List<DraftActivityEvidence> activityEvidences) {
        return activityEvidences.stream()
                .map(e -> new GetDraftActivityEvidenceResponse(
                        e.getId(),
                        e.getTitle(),
                        e.getContent(),
                        e.getImageUrl(),
                        e.getCategoryName(),
                        e.getEvidenceType()
                ))
                .toList();
    }

    private List<GetDraftReadingEvidenceResponse> createDraftReadingResponse(List<DraftReadingEvidence> readingEvidences) {
        return readingEvidences.stream()
                .map(e -> new GetDraftReadingEvidenceResponse(
                        e.getId(),
                        e.getTitle(),
                        e.getAuthor(),
                        e.getPage(),
                        e.getContent()
                ))
                .toList();
    }
}
