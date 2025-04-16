package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incude.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.ReadingEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.usecase.FindEvidenceUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.ReadingEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.presentation.data.GetActivityEvidenceDto;
import team.incude.gsmc.v2.domain.evidence.presentation.data.GetOtherEvidenceDto;
import team.incude.gsmc.v2.domain.evidence.presentation.data.GetReadingEvidenceDto;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetEvidencesResponse;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.global.security.jwt.usecase.service.CurrentMemberProvider;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FineEvidenceByCurrentUserService implements FindEvidenceUseCase {

    private final ActivityEvidencePersistencePort activityEvidencePersistencePort;
    private final ReadingEvidencePersistencePort readingEvidencePersistencePort;
    private final OtherEvidencePersistencePort otherEvidencePersistencePort;
    private final CurrentMemberProvider currentMemberProvider;

    @Override
    @Transactional(readOnly = true)
    public GetEvidencesResponse execute() {
        Member member = currentMemberProvider.getCurrentUser();
        return findEvidence(member.getEmail());
    }

    @Override
    @Transactional(readOnly = true)
    public GetEvidencesResponse execute(String email) {
        return findEvidence(email);
    }

    private GetEvidencesResponse findEvidence(String email) {
        List<ActivityEvidence> majorEvidences = activityEvidencePersistencePort.findActivityEvidenceByEmailAndEvidenceType(email, EvidenceType.MAJOR);
        List<ActivityEvidence> humanitiesEvidences = activityEvidencePersistencePort.findActivityEvidenceByEmailAndEvidenceType(email, EvidenceType.HUMANITIES);
        List<OtherEvidence> otherEvidences = otherEvidencePersistencePort.findOtherEvidenceByEmail(email);
        List<ReadingEvidence> readingEvidences = readingEvidencePersistencePort.findReadingEvidenceByEmail(email);

        List<GetActivityEvidenceDto> majorEvidenceDtos = createActivityEvidenceDtos(majorEvidences);
        List<GetActivityEvidenceDto> humanitiesEvidenceDtos = createActivityEvidenceDtos(humanitiesEvidences);
        List<GetOtherEvidenceDto> otherEvidenceDtos = createOtherEvidenceDtos(otherEvidences);
        List<GetReadingEvidenceDto> readingEvidenceDtos = createReadingEvidenceDtos(readingEvidences);

        return new GetEvidencesResponse(majorEvidenceDtos, humanitiesEvidenceDtos, readingEvidenceDtos, otherEvidenceDtos);
    }

    private List<GetActivityEvidenceDto> createActivityEvidenceDtos(List<ActivityEvidence> evidences) {
        return evidences.stream()
                .map(e -> new GetActivityEvidenceDto(
                        e.getId().getId(),
                        e.getTitle(),
                        e.getContent(),
                        e.getImageUrl(),
                        e.getId().getReviewStatus(),
                        e.getId().getScore().getCategory().getName()
                ))
                .toList();
    }

    private List<GetOtherEvidenceDto> createOtherEvidenceDtos(List<OtherEvidence> evidences) {
        return evidences.stream()
                .map(e -> new GetOtherEvidenceDto(
                        e.getId().getId(),
                        e.getFileUri(),
                        e.getId().getEvidenceType(),
                        e.getId().getReviewStatus(),
                        e.getId().getScore().getCategory().getName()
                ))
                .toList();
    }

    private List<GetReadingEvidenceDto> createReadingEvidenceDtos(List<ReadingEvidence> evidences) {
        return evidences.stream()
                .map(e -> new GetReadingEvidenceDto(
                        e.getId().getId(),
                        e.getTitle(),
                        e.getAuthor(),
                        e.getPage(),
                        e.getContent(),
                        e.getId().getReviewStatus()
                ))
                .toList();
    }
}
