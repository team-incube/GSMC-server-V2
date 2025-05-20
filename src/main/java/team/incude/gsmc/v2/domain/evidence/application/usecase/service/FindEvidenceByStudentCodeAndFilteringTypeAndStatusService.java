package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incude.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.ReadingEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.usecase.FindEvidenceByStudentCodeAndFilteringTypeAndStatusUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.ReadingEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetActivityEvidenceResponse;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetEvidencesResponse;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetOtherEvidenceResponse;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetReadingEvidenceResponse;

import java.util.List;

/**
 * 학생 코드, 증빙자료 유형, 검토 상태를 기반으로 증빙자료를 조회하는 유스케이스 구현 클래스입니다.
 *
 * <p>{@link FindEvidenceByStudentCodeAndFilteringTypeAndStatusUseCase}를 구현하며,
 * 관리자가 특정 학생의 증빙자료를 유형별(MAJOR, HUMANITIES, READING, OTHER) 및 검토 상태별로 필터링하여 조회할 수 있도록 합니다.
 *
 * <p>각 유형별로 도메인 객체를 조회한 뒤, 응답 DTO로 변환하여 통합된 응답 객체로 반환합니다.
 *
 * @author suuuuuuminnnnnn
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindEvidenceByStudentCodeAndFilteringTypeAndStatusService implements FindEvidenceByStudentCodeAndFilteringTypeAndStatusUseCase {

    private final ActivityEvidencePersistencePort activityEvidencePersistencePort;
    private final ReadingEvidencePersistencePort readingEvidencePersistencePort;
    private final OtherEvidencePersistencePort otherEvidencePersistencePort;

    /**
     * 학생 코드, 증빙자료 유형, 검토 상태를 기반으로 증빙자료를 필터링하여 조회합니다.
     * <p>활동 증빙자료는 내부적으로 전공(MAJOR)과 인문(HUMANITIES)으로 분류되며,
     * 기타 및 독서 증빙자료는 직접 검색 후 DTO로 변환됩니다.
     * @param studentCode 학생 학번
     * @param type 필터링할 증빙자료 타입
     * @param status 필터링할 검토 상태
     * @return 분류된 증빙자료 DTO 목록을 포함한 응답 객체
     */
    @Override
    public GetEvidencesResponse execute(String studentCode, EvidenceType type, ReviewStatus status) {
        List<ActivityEvidence> activityEvidences = activityEvidencePersistencePort.searchActivityEvidence(studentCode, type, null, status, null, null);

        List<ActivityEvidence> majorEvidences = filterByType(activityEvidences, EvidenceType.MAJOR);
        List<ActivityEvidence> humanitiesEvidences = filterByType(activityEvidences, EvidenceType.HUMANITIES);
        List<OtherEvidence> otherEvidences = otherEvidencePersistencePort.searchOtherEvidence(studentCode, type, status, null, null);
        List<ReadingEvidence> readingEvidences = readingEvidencePersistencePort.searchReadingEvidence(studentCode, null, type, status, null, null);

        List<GetActivityEvidenceResponse> majorEvidenceDto = createActivityEvidenceDtos(majorEvidences);
        List<GetActivityEvidenceResponse> humanitiesEvidenceDto = createActivityEvidenceDtos(humanitiesEvidences);
        List<GetReadingEvidenceResponse> readingEvidenceDto = createReadingEvidenceDtos(readingEvidences);
        List<GetOtherEvidenceResponse> otherEvidenceDto = createOtherEvidenceDtos(otherEvidences);

        return new GetEvidencesResponse(majorEvidenceDto, humanitiesEvidenceDto, readingEvidenceDto, otherEvidenceDto);
    }

    /**
     * 활동 증빙자료 중 특정 타입에 해당하는 항목만 필터링합니다.
     * @param evidences 활동 증빙자료 리스트
     * @param type 필터링할 증빙자료 타입
     * @return 필터링된 활동 증빙자료 리스트
     */
    private List<ActivityEvidence> filterByType(List<ActivityEvidence> evidences, EvidenceType type) {
        return evidences.stream()
                .filter(e -> e.getId().getEvidenceType().equals(type))
                .toList();
    }

    /**
     * 활동 증빙자료 도메인 리스트를 DTO 리스트로 변환합니다.
     * @param evidences 활동 증빙자료 리스트
     * @return 변환된 응답 DTO 리스트
     */
    private List<GetActivityEvidenceResponse> createActivityEvidenceDtos(List<ActivityEvidence> evidences) {
        return evidences.stream()
                .map(e -> new GetActivityEvidenceResponse(
                        e.getId().getId(),
                        e.getTitle(),
                        e.getContent(),
                        e.getImageUrl(),
                        e.getId().getReviewStatus(),
                        e.getId().getScore().getCategory().getName()
                ))
                .toList();
    }

    /**
     * 기타 증빙자료 도메인 리스트를 DTO 리스트로 변환합니다.
     * @param evidences 기타 증빙자료 리스트
     * @return 변환된 응답 DTO 리스트
     */
    private List<GetOtherEvidenceResponse> createOtherEvidenceDtos(List<OtherEvidence> evidences) {
        return evidences.stream()
                .map(e -> new GetOtherEvidenceResponse(
                        e.getId().getId(),
                        e.getFileUri(),
                        e.getId().getEvidenceType(),
                        e.getId().getReviewStatus(),
                        e.getId().getScore().getCategory().getName()
                ))
                .toList();
    }

    /**
     * 독서 증빙자료 도메인 리스트를 DTO 리스트로 변환합니다.
     * @param evidences 독서 증빙자료 리스트
     * @return 변환된 응답 DTO 리스트
     */
    private List<GetReadingEvidenceResponse> createReadingEvidenceDtos(List<ReadingEvidence> evidences) {
        return evidences.stream()
                .map(e -> new GetReadingEvidenceResponse(
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