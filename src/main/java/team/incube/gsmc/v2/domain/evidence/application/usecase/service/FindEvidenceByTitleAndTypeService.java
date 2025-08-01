package team.incube.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.application.port.ReadingEvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.application.usecase.FindEvidenceByTitleAndTypeUseCase;
import team.incube.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incube.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incube.gsmc.v2.domain.evidence.domain.ReadingEvidence;
import team.incube.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incube.gsmc.v2.domain.evidence.presentation.data.response.GetActivityEvidenceResponse;
import team.incube.gsmc.v2.domain.evidence.presentation.data.response.GetEvidencesResponse;
import team.incube.gsmc.v2.domain.evidence.presentation.data.response.GetOtherEvidenceResponse;
import team.incube.gsmc.v2.domain.evidence.presentation.data.response.GetReadingEvidenceResponse;
import team.incube.gsmc.v2.domain.member.domain.Member;
import team.incube.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;

import java.util.List;

import static team.incube.gsmc.v2.domain.evidence.domain.constant.EvidenceTypeGroup.OTHER_TYPES;

/**
 * 학생 코드, 증빙자료 제목, 증빙자료 유형을 기준으로 증빙자료를 조회하는 유스케이스 구현 클래스입니다.
 * <p>{@link FindEvidenceByTitleAndTypeUseCase}를 구현하며,
 * 관리자 또는 검토자가 특정 학생의 증빙자료 중 조건에 맞는 항목을 필터링하여 조회할 수 있도록 합니다.
 * <p>전공, 인문 활동 증빙자료는 내부적으로 구분되며, 기타 및 독서 증빙자료는 직접 필터링 후 DTO로 변환됩니다.
 * @author suuuuuuminnnnnn
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindEvidenceByTitleAndTypeService implements FindEvidenceByTitleAndTypeUseCase {

    private final CurrentMemberProvider currentMemberProvider;
    private final OtherEvidencePersistencePort otherEvidencePersistencePort;
    private final ReadingEvidencePersistencePort readingEvidencePersistencePort;
    private final ActivityEvidencePersistencePort activityEvidencePersistencePort;

    /**
     * 학생 코드, 제목, 증빙자료 유형을 기반으로 증빙자료를 조회합니다.
     * <p>활동 증빙자료는 전공(MAJOR)과 인문(HUMANITIES)으로 구분되어 필터링되며,
     * 기타 및 독서 증빙자료는 별도 저장소에서 조건에 맞는 항목을 조회합니다.
     * @param title 증빙자료 제목 키워드
     * @param evidenceType 필터링할 증빙자료 유형
     * @return 조회된 증빙자료 목록 DTO 응답
     */
    @Override
    public GetEvidencesResponse execute(String title, EvidenceType evidenceType) {
        Member member = currentMemberProvider.getCurrentUser();

        return createEvidencesResponse(member.getEmail(), title, evidenceType);
    }

    /**
     * 학생 코드, 제목, 증빙자료 유형을 기반으로 증빙자료를 조회하여 응답 DTO를 생성합니다.
     * <p>활동 증빙자료는 전공(MAJOR)과 인문(HUMANITIES)으로 구분되며,
     * 기타 및 독서 증빙자료는 별도로 조회하여 응답 DTO로 변환합니다.
     * @param email 학생 이메일
     * @param title 증빙자료 제목 키워드
     * @param evidenceType 필터링할 증빙자료 유형
     * @return 조회된 증빙자료 목록 응답 DTO
     */
    private GetEvidencesResponse createEvidencesResponse(String email, String title, EvidenceType evidenceType) {
        List<ActivityEvidence> majorEvidences = findMajorEvidences(email, title, evidenceType);
        List<ActivityEvidence> humanitiesEvidences = findHumanitiesEvidences(email, title, evidenceType);
        List<ReadingEvidence> readingEvidences = findReadingEvidences(email, title, evidenceType);
        List<OtherEvidence> otherEvidences = findOtherEvidences(email, title, evidenceType);

        return new GetEvidencesResponse(
                createActivityEvidenceDtos(majorEvidences),
                createActivityEvidenceDtos(humanitiesEvidences),
                createReadingEvidenceDtos(readingEvidences),
                createOtherEvidenceDtos(otherEvidences)
        );
    }

    /**
     * 활동 증빙자료를 전공(MAJOR) 또는 인문(HUMANITIES)으로 필터링하여 조회합니다.
     * <p>증빙자료 유형이 null이거나 MAJOR인 경우 전공 증빙자료를 조회하고,
     * HUMANITIES인 경우 인문 증빙자료를 조회합니다.
     * @param email 학생 이메일
     * @param title 증빙자료 제목 키워드
     * @param evidenceType 필터링할 증빙자료 유형
     * @return 조회된 활동 증빙자료 목록
     */
    private List<ActivityEvidence> findMajorEvidences(String email, String title, EvidenceType evidenceType) {
        if (evidenceType == null || evidenceType.equals(EvidenceType.MAJOR)) {
            return findActivityEvidence(email, title, EvidenceType.MAJOR);
        }
        return List.of();
    }

    /**
     * 활동 증빙자료를 인문(HUMANITIES)으로 필터링하여 조회합니다.
     * <p>증빙자료 유형이 null이거나 HUMANITIES인 경우 인문 증빙자료를 조회합니다.
     * @param email 학생 이메일
     * @param title 증빙자료 제목 키워드
     * @param evidenceType 필터링할 증빙자료 유형
     * @return 조회된 인문 활동 증빙자료 목록
     */
    private List<ActivityEvidence> findHumanitiesEvidences(String email, String title, EvidenceType evidenceType) {
        if (evidenceType == null || evidenceType.equals(EvidenceType.HUMANITIES)) {
            return findActivityEvidence(email, title, EvidenceType.HUMANITIES);
        }
        return List.of();
    }

    /**
     * 독서 증빙자료를 조회합니다.
     * <p>증빙자료 유형이 null이거나 READING인 경우 독서 증빙자료를 조회합니다.
     * @param email 학생 이메일
     * @param title 증빙자료 제목 키워드
     * @param evidenceType 필터링할 증빙자료 유형
     * @return 조회된 독서 증빙자료 목록
     */
    private List<ReadingEvidence> findReadingEvidences(String email, String title, EvidenceType evidenceType) {
        if (evidenceType == null || evidenceType.equals(EvidenceType.READING)) {
            return findReadingEvidence(email, title);
        }
        return List.of();
    }

    /**
     * 기타 증빙자료를 조회합니다.
     * <p>증빙자료 유형이 null인 경우 모든 기타 증빙자료를 조회하고,
     * 특정 유형이 OTHER_TYPES에 포함된 경우 해당 유형의 기타 증빙자료를 조회합니다.
     * @param email 학생 이메일
     * @param title 증빙자료 제목 키워드
     * @param evidenceType 필터링할 증빙자료 유형
     * @return 조회된 기타 증빙자료 목록
     */
    private List<OtherEvidence> findOtherEvidences(String email, String title, EvidenceType evidenceType) {
        if (evidenceType == null && title == null) {
            return otherEvidencePersistencePort.findOtherEvidenceByEmail(email);
        } else if (evidenceType != null && OTHER_TYPES.contains(evidenceType) && title == null) {
            return otherEvidencePersistencePort.findOtherEvidenceByMemberEmailAndType(email, evidenceType);
        }
        return List.of();
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
     * 활동 증빙자료를 학생 이메일, 제목, 증빙자료 유형을 기준으로 조회합니다.
     * @param email 학생 이메일
     * @param title 증빙자료 제목 키워드
     * @param type 증빙자료 유형
     * @return 조회된 활동 증빙자료 목록
     */
    private List<ActivityEvidence> findActivityEvidence(String email, String title, EvidenceType type) {
        return activityEvidencePersistencePort.findActivityEvidenceByMemberEmailAndTitleAndType(email, title, type);
    }

    /**
     * 독서 증빙자료를 학생 이메일, 제목을 기준으로 조회합니다.
     * @param email 학생 이메일
     * @param title 증빙자료 제목 키워드
     * @return 조회된 독서 증빙자료 목록
     */
    private List<ReadingEvidence> findReadingEvidence(String email, String title) {
        return readingEvidencePersistencePort.findReadingEvidenceByEmailAndTitleAndType(email, title, EvidenceType.READING);
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
}