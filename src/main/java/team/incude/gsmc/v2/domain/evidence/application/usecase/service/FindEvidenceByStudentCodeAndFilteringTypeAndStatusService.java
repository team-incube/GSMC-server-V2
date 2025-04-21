package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.ReadingEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.usecase.FindEvidenceByStudentCodeAndFilteringTypeAndStatusUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.ReadingEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.evidence.presentation.data.GetActivityEvidenceDto;
import team.incude.gsmc.v2.domain.evidence.presentation.data.GetOtherEvidenceDto;
import team.incude.gsmc.v2.domain.evidence.presentation.data.GetReadingEvidenceDto;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetEvidencesResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindEvidenceByStudentCodeAndFilteringTypeAndStatusService implements FindEvidenceByStudentCodeAndFilteringTypeAndStatusUseCase {

    private final ActivityEvidencePersistencePort activityEvidencePersistencePort;
    private final ReadingEvidencePersistencePort readingEvidencePersistencePort;
    private final OtherEvidencePersistencePort otherEvidencePersistencePort;

    @Override
    public GetEvidencesResponse execute(String studentCode, EvidenceType type, ReviewStatus status) {
        List<ActivityEvidence> activityEvidences = activityEvidencePersistencePort.findActivityEvidenceByStudentCodeAndTypeAndTitleAndStatusAndGradeAndClassNumber(studentCode, type, null, status, null, null);

        List<ActivityEvidence> majorEvidences = filterByType(activityEvidences, type);
        List<ActivityEvidence> humanitiesEvidences = filterByType(activityEvidences, type);
        List<OtherEvidence> otherEvidences = otherEvidencePersistencePort.findOtherEvidenceByStudentCodeAndTypeAndStatusAndGradeAndClassNumber(studentCode, type, status, null, null);
        List<ReadingEvidence> readingEvidences = readingEvidencePersistencePort.findReadingEvidenceByStudentCodeAndTitleAndTypeAndStatusAndGradeAndClassNumber(studentCode, null, type, status, null, null);

        List<GetActivityEvidenceDto> majorEvidenceDto = createActivityEvidenceDtos(majorEvidences);
        List<GetActivityEvidenceDto> humanitiesEvidenceDto = createActivityEvidenceDtos(humanitiesEvidences);
        List<GetReadingEvidenceDto> readingEvidenceDto = createReadingEvidenceDtos(readingEvidences);
        List<GetOtherEvidenceDto> otherEvidenceDto = createOtherEvidenceDtos(otherEvidences);

        return new GetEvidencesResponse(majorEvidenceDto, humanitiesEvidenceDto, readingEvidenceDto, otherEvidenceDto);
    }

    private List<ActivityEvidence> filterByType(List<ActivityEvidence> evidences, EvidenceType type) {
        return evidences.stream()
                .filter(e -> e.getId().getEvidenceType().equals(type))
                .toList();
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
