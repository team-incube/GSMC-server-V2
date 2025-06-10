package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incude.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.ReadingEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.usecase.FindEvidenceByEmailAndTypeAndStatusUseCase;
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
 * <p>{@link FindEvidenceByEmailAndTypeAndStatusUseCase}를 구현하며,
 * 관리자가 특정 학생의 증빙자료를 유형별(MAJOR, HUMANITIES, READING, OTHER) 및 검토 상태별로 필터링하여 조회할 수 있도록 합니다.
 * <p>각 유형별로 도메인 객체를 조회한 뒤, 응답 DTO로 변환하여 통합된 응답 객체로 반환합니다.
 * @author suuuuuuminnnnnn
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindEvidenceByEmailAndTypeAndStatusService implements FindEvidenceByEmailAndTypeAndStatusUseCase {

    private final ActivityEvidencePersistencePort activityEvidencePersistencePort;
    private final ReadingEvidencePersistencePort readingEvidencePersistencePort;
    private final OtherEvidencePersistencePort otherEvidencePersistencePort;

    /**
     * 학생 이메일, 증빙자료 유형, 검토 상태를 기반으로 증빙자료를 조회합니다.
     * <p>증빙자료 유형이 null인 경우 모든 유형의 증빙자료를 조회하며,
     * 각 유형별로 필터링된 결과를 응답 DTO로 변환하여 반환합니다.
     * @param email 학생 이메일
     * @param type 증빙자료 유형 (MAJOR, HUMANITIES, READING, OTHER)
     * @param status 검토 상태 (APPROVED, REJECTED, PENDING 등)
     * @return 조회된 증빙자료 목록 응답 DTO
     */
    @Override
    public GetEvidencesResponse execute(String email, EvidenceType type, ReviewStatus status) {
        if (type == null) {
            return findAllEvidenceByEmailAndStatus(email, status);
        }

        return findEvidenceByEmailAndTypeAndStatus(email, type, status);
    }

    /**
     * 학생 이메일을 기준으로 모든 유형의 증빙자료를 검토 상태별로 조회합니다.
     * <p>활동 증빙자료는 전공(MAJOR)과 인문(HUMANITIES)으로 구분되며,
     * 독서 및 기타 증빙자료는 별도로 조회하여 응답 DTO로 변환합니다.
     * @param email 학생 이메일
     * @param status 검토 상태 (APPROVED, REJECTED, PENDING 등)
     * @return 모든 유형의 증빙자료 목록 응답 DTO
     */
    private GetEvidencesResponse findAllEvidenceByEmailAndStatus(String email, ReviewStatus status) {
        List<ActivityEvidence> allActivities = activityEvidencePersistencePort.findActivityEvidenceByMemberEmailAndTypeAndStatus(email, null, status);
        List<ActivityEvidence> majorEvidences = filterByType(allActivities, EvidenceType.MAJOR);
        List<ActivityEvidence> humanitiesEvidences = filterByType(allActivities, EvidenceType.HUMANITIES);
        List<ReadingEvidence> readingEvidences = readingEvidencePersistencePort.findReadingEvidenceByEmailAndStatus(email, status);
        List<OtherEvidence> otherEvidences = otherEvidencePersistencePort.findOtherEvidenceByMemberEmailAndTypeAndStatus(email, null, status);

        return new GetEvidencesResponse(
                createActivityEvidenceDtos(majorEvidences),
                createActivityEvidenceDtos(humanitiesEvidences),
                createReadingEvidenceDtos(readingEvidences),
                createOtherEvidenceDtos(otherEvidences)
        );
    }

    /**
     * 학생 이메일, 증빙자료 유형, 검토 상태를 기준으로 증빙자료를 조회합니다.
     * <p>활동 증빙자료는 전공(MAJOR)과 인문(HUMANITIES)으로 구분되며,
     * 독서 및 기타 증빙자료는 별도로 조회하여 응답 DTO로 변환합니다.
     * @param email 학생 이메일
     * @param type 증빙자료 유형 (MAJOR, HUMANITIES, READING, OTHER)
     * @param status 검토 상태 (APPROVED, REJECTED, PENDING 등)
     * @return 특정 유형의 증빙자료 목록 응답 DTO
     */
    private GetEvidencesResponse findEvidenceByEmailAndTypeAndStatus(String email, EvidenceType type, ReviewStatus status) {
        List<ActivityEvidence> majorEvidences = List.of();
        List<ActivityEvidence> humanitiesEvidences = List.of();
        List<ReadingEvidence> readingEvidences = List.of();
        List<OtherEvidence> otherEvidences = List.of();

        if (type == EvidenceType.MAJOR) {
            majorEvidences = activityEvidencePersistencePort.findActivityEvidenceByMemberEmailAndTypeAndStatus(email, type, status);
        } else if (type == EvidenceType.HUMANITIES) {
            humanitiesEvidences = activityEvidencePersistencePort.findActivityEvidenceByMemberEmailAndTypeAndStatus(email, type, status);
        } else if (type == EvidenceType.READING) {
            readingEvidences = readingEvidencePersistencePort.findReadingEvidenceByEmailAndStatus(email, status);
        } else {
            otherEvidences = otherEvidencePersistencePort.findOtherEvidenceByMemberEmailAndTypeAndStatus(email, type, status);
        }

        return new GetEvidencesResponse(
                createActivityEvidenceDtos(majorEvidences),
                createActivityEvidenceDtos(humanitiesEvidences),
                createReadingEvidenceDtos(readingEvidences),
                createOtherEvidenceDtos(otherEvidences)
        );
    }

    /**
     * 증빙자료 목록을 유형별로 필터링합니다.
     * <p>활동 증빙자료의 경우 전공(MAJOR)과 인문(HUMANITIES)으로 구분하여 필터링합니다.
     * @param evidences 증빙자료 목록
     * @param type 필터링할 증빙자료 유형
     * @return 필터링된 증빙자료 목록
     */
    private List<ActivityEvidence> filterByType(List<ActivityEvidence> evidences, EvidenceType type) {
        return evidences.stream()
                .filter(e -> e.getId().getEvidenceType().equals(type))
                .toList();
    }

    /**
     * 활동 증빙자료 도메인 리스트를 응답 DTO 리스트로 변환합니다.
     * @param evidences 활동 증빙자료 목록
     * @return 활동 증빙자료 응답 DTO 목록
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
     * 독서 증빙자료 도메인 리스트를 응답 DTO 리스트로 변환합니다.
     * @param evidences 독서 증빙자료 목록
     * @return 독서 증빙자료 응답 DTO 목록
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

    /**
     * 기타 증빙자료 도메인 리스트를 응답 DTO 리스트로 변환합니다.
     * @param evidences 기타 증빙자료 목록
     * @return 기타 증빙자료 응답 DTO 목록
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
}