package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incude.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.ReadingEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.usecase.FindEvidenceByEmailAndTitleAndTypeUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.ReadingEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.presentation.data.GetActivityEvidenceDto;
import team.incude.gsmc.v2.domain.evidence.presentation.data.GetOtherEvidenceDto;
import team.incude.gsmc.v2.domain.evidence.presentation.data.GetReadingEvidenceDto;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetEvidencesResponse;
import team.incude.gsmc.v2.domain.member.application.port.TeacherDetailPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.HomeroomTeacherDetail;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.member.domain.constant.MemberRole;
import team.incude.gsmc.v2.global.security.jwt.usecase.service.CurrentMemberProvider;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindEvidenceByEmailAndTitleAndTypeService implements FindEvidenceByEmailAndTitleAndTypeUseCase {

    private final ActivityEvidencePersistencePort activityEvidencePersistencePort;
    private final ReadingEvidencePersistencePort readingEvidencePersistencePort;
    private final OtherEvidencePersistencePort otherEvidencePersistencePort;
    private final CurrentMemberProvider currentMemberProvider;
    private final TeacherDetailPersistencePort teacherDetailPersistencePort;

    @Override
    @Transactional(readOnly = true)
    public GetEvidencesResponse execute(String email, String title, EvidenceType evidenceType) {
        Member member = currentMemberProvider.getCurrentUser();
        Integer grade = null;
        Integer classNumber = null;

        if (member.getRole().equals(MemberRole.ROLE_MAISTER_PART_TEACHER)) {
            grade = null;
            classNumber = null;
        } else if (member.getRole().equals(MemberRole.ROLE_HOMEROOM_TEACHER)) {
            HomeroomTeacherDetail homeroomTeacherDetail = teacherDetailPersistencePort.findTeacherDetailByEmail(member.getEmail());
            grade = homeroomTeacherDetail.getGrade();
            classNumber = homeroomTeacherDetail.getClassNumber();
        }

        List<ActivityEvidence> activityEvidences = activityEvidencePersistencePort.findActivityEvidenceByEmailAndTypeAndTitleAndGradeAndClassNumber(email, evidenceType, title, grade, classNumber);

        List<ActivityEvidence> majorEvidences = filterByType(activityEvidences, EvidenceType.MAJOR);
        List<ActivityEvidence> humanitiesEvidences = filterByType(activityEvidences, EvidenceType.HUMANITIES);
        List<OtherEvidence> otherEvidences = otherEvidencePersistencePort.findOtherEvidenceByEmailAndTypeAndGradeAndClassNumber(email, evidenceType, grade, classNumber);
        List<ReadingEvidence> readingEvidences = readingEvidencePersistencePort.findReadingEvidenceByEmailAndTitleAndTypeAndGradeAndClassNumber(email, title, evidenceType, grade, classNumber);

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
