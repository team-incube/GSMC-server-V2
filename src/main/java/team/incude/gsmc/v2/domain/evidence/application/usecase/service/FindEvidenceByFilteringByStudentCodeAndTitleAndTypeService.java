package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incude.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.ReadingEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.usecase.FindEvidenceByFilteringByStudentCodeAndTitleAndTypeUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.ReadingEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.presentation.data.GetActivityEvidenceDto;
import team.incude.gsmc.v2.domain.evidence.presentation.data.GetOtherEvidenceDto;
import team.incude.gsmc.v2.domain.evidence.presentation.data.GetReadingEvidenceDto;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetEvidencesResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindEvidenceByFilteringByStudentCodeAndTitleAndTypeService implements FindEvidenceByFilteringByStudentCodeAndTitleAndTypeUseCase {

    private final OtherEvidencePersistencePort otherEvidencePersistencePort;
    private final ReadingEvidencePersistencePort readingEvidencePersistencePort;
    private final ActivityEvidencePersistencePort activityEvidencePersistencePort;

    @Override
    @Transactional(readOnly = true)
    public GetEvidencesResponse execute(String studentCode, String title, EvidenceType evidenceType) {
        List<ActivityEvidence> activityEvidences = activityEvidencePersistencePort.findActivityEvidenceByStudentCodeAndTypeAndTitleAndStatusAndGradeAndClassNumber(studentCode, evidenceType, title, null, null, null);

        List<ActivityEvidence> majorEvidences = filterByType(activityEvidences, EvidenceType.MAJOR);
        List<ActivityEvidence> humanitiesEvidences = filterByType(activityEvidences, EvidenceType.HUMANITIES);
        List<OtherEvidence> otherEvidences = otherEvidencePersistencePort.findOtherEvidenceByStudentCodeAndTypeAndStatusAndGradeAndClassNumber(studentCode, evidenceType, null, null, null);
        List<ReadingEvidence> readingEvidences = readingEvidencePersistencePort.findReadingEvidenceByStudentCodeAndTitleAndTypeAndStatusAndGradeAndClassNumber(studentCode, title, evidenceType, null, null, null);

        List<GetActivityEvidenceDto> majorEvidenceDtos = createActivityEvidenceDtos(majorEvidences);
        List<GetActivityEvidenceDto> humanitiesEvidenceDtos = createActivityEvidenceDtos(humanitiesEvidences);
        List<GetOtherEvidenceDto> otherEvidenceDtos = createOtherEvidenceDtos(otherEvidences);
        List<GetReadingEvidenceDto> readingEvidenceDtos = createReadingEvidenceDtos(readingEvidences);

        return new GetEvidencesResponse(majorEvidenceDtos, humanitiesEvidenceDtos, readingEvidenceDtos, otherEvidenceDtos);
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
