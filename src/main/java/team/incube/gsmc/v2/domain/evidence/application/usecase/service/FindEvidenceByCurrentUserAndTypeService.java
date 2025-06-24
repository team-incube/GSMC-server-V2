package team.incube.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.application.port.ReadingEvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.application.usecase.FindEvidenceByCurrentUserAndTypeUseCase;
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

/**
 * 현재 사용자 기준으로 증빙자료를 유형별로 조회하는 유스케이스 구현 클래스입니다.
 * <p>{@link FindEvidenceByCurrentUserAndTypeUseCase}를 구현하며,
 * 활동, 독서, 기타 증빙자료를 사용자의 이메일 기준으로 필터링하여 조회합니다.
 * <p>활동 증빙자료는 {@link EvidenceType}을 기준으로 전공(MAJOR)과 인문(HUMANITIES)으로 나뉘며,
 * 각각 별도로 분리된 응답 객체 리스트로 매핑됩니다.
 * @author suuuuuuminnnnnn
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindEvidenceByCurrentUserAndTypeService implements FindEvidenceByCurrentUserAndTypeUseCase {

    private final ActivityEvidencePersistencePort activityEvidencePersistencePort;
    private final ReadingEvidencePersistencePort readingEvidencePersistencePort;
    private final OtherEvidencePersistencePort otherEvidencePersistencePort;
    private final CurrentMemberProvider currentMemberProvider;

    /**
     * 현재 로그인한 사용자의 증빙자료를 조회하고 유형별로 응답 DTO로 변환합니다.
     * <p>활동 증빙자료는 전공(MAJOR)과 인문(HUMANITIES)으로 구분되며,
     * 독서 및 기타 증빙자료는 별도로 필터링 없이 조회됩니다.
     * @param type 활동 증빙자료 필터링을 위한 증빙자료 유형 (MAJOR 또는 HUMANITIES)
     * @return 사용자별 증빙자료 목록 응답 DTO
     */
    @Override
    public GetEvidencesResponse execute(EvidenceType type) {
        Member member = currentMemberProvider.getCurrentUser();
        String email = member.getEmail();

        if (type == null) {
            return findAllEvidenceByEmail(email);
        }

        return findEvidenceByEmailAndType(email, type);
    }

    private GetEvidencesResponse findAllEvidenceByEmail(String email) {
        List<ActivityEvidence> activityEvidences = activityEvidencePersistencePort.findActivityEvidenceByMemberEmail(email);

        List<ActivityEvidence> majorEvidences = filterByType(activityEvidences, EvidenceType.MAJOR);
        List<ActivityEvidence> humanitiesEvidences = filterByType(activityEvidences, EvidenceType.HUMANITIES);
        List<OtherEvidence> otherEvidences = otherEvidencePersistencePort.findOtherEvidenceByEmail(email);
        List<ReadingEvidence> readingEvidences = readingEvidencePersistencePort.findReadingEvidenceByEmail(email);

        return new GetEvidencesResponse(
                createActivityEvidenceDtos(majorEvidences),
                createActivityEvidenceDtos(humanitiesEvidences),
                createReadingEvidenceDtos(readingEvidences),
                createOtherEvidenceDtos(otherEvidences)
        );
    }

    private GetEvidencesResponse findEvidenceByEmailAndType(String email, EvidenceType type) {
        List<ActivityEvidence> majorEvidences = List.of();
        List<ActivityEvidence> humanitiesEvidences = List.of();
        List<ReadingEvidence> readingEvidences = List.of();
        List<OtherEvidence> otherEvidences = List.of();

        if (type == EvidenceType.MAJOR) {
            majorEvidences = findActivityEvidenceByEmailAndType(email, EvidenceType.MAJOR);
        } else if (type == EvidenceType.HUMANITIES) {
            humanitiesEvidences = findActivityEvidenceByEmailAndType(email, EvidenceType.HUMANITIES);
        } else if (type == EvidenceType.READING) {
            readingEvidences = readingEvidencePersistencePort.findReadingEvidenceByEmail(email);
        } else {
            otherEvidences = otherEvidencePersistencePort.findOtherEvidenceByMemberEmailAndType(email, type);
        }

        return new GetEvidencesResponse(
                createActivityEvidenceDtos(majorEvidences),
                createActivityEvidenceDtos(humanitiesEvidences),
                createReadingEvidenceDtos(readingEvidences),
                createOtherEvidenceDtos(otherEvidences)
        );
    }


    /**
     * 활동 증빙자료 리스트에서 특정 EvidenceType에 해당하는 항목만 필터링합니다.
     * @param evidences 활동 증빙자료 목록
     * @param type 필터링할 증빙자료 타입
     * @return 필터링된 활동 증빙자료 목록
     */
    private List<ActivityEvidence> filterByType(List<ActivityEvidence> evidences, EvidenceType type) {
        return evidences.stream()
                .filter(e -> e.getId().getEvidenceType().equals(type))
                .toList();
    }

    private List<ActivityEvidence> findActivityEvidenceByEmailAndType(String email, EvidenceType type) {
        return activityEvidencePersistencePort.findActivityEvidenceByEmailAndEvidenceType(email, type);
    }

    /**
     * 활동 증빙자료 도메인 리스트를 응답 DTO 리스트로 변환합니다.
     * @param evidences 활동 증빙자료 리스트
     * @return 응답 DTO 리스트
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
     * 기타 증빙자료 도메인 리스트를 응답 DTO 리스트로 변환합니다.
     * @param evidences 기타 증빙자료 리스트
     * @return 응답 DTO 리스트
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
     * @param evidences 독서 증빙자료 리스트
     * @return 응답 DTO 리스트
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