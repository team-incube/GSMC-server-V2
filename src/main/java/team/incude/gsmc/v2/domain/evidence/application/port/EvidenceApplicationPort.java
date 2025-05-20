package team.incude.gsmc.v2.domain.evidence.application.port;

import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.*;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.util.UUID;

/**
 * 증빙자료 관련 기능을 정의하는 애플리케이션 계층 포트 인터페이스입니다.
 * <p>임시저장, 생성, 수정, 삭제, 상태 변경, 조회 등의 주요 유스케이스를 정의하며,
 * Web 어댑터 계층으로부터 전달받은 요청을 도메인 로직으로 위임합니다.
 * <p>증빙자료는 활동, 독서, 기타 유형으로 나뉘며,
 * 각 작업은 사용자 권한 또는 학생 코드 기준으로 수행될 수 있습니다.
 * 이 인터페이스는 {@code @Port} 어노테이션을 통해 INBOUND 방향으로 지정되어 있습니다.
 * @author suuuuuuminnnnnn
 */
@Port(direction = PortDirection.INBOUND)
public interface EvidenceApplicationPort {
    GetEvidencesResponse findEvidenceByCurrentUserAndType(EvidenceType evidenceType);

    GetEvidencesResponse findEvidenceByStudentCodeAndTypeAndStatus(String studentCode, EvidenceType evidenceType, ReviewStatus status);

    GetEvidencesResponse findEvidenceByStudentCodeAndTitleAndType(String  studentCode, String title, EvidenceType evidenceType);

    void updateMajorEvidenceByCurrentUser(Long evidenceId, String title, String content, MultipartFile file, String imageUrl);

    void updateHumanitiesEvidenceByCurrentUser(Long evidenceId, String title, String content, MultipartFile file, String imageUrl);

    void updateReadingEvidenceByCurrentUser(Long evidenceId, String title, String author, String content, int page);

    void updateOtherEvidenceByCurrentUser(Long evidenceId, MultipartFile file, String imageUrl);

    void deleteEvidence(Long evidenceId);

    void createActivityEvidence(String categoryName, String title, String content, MultipartFile file, String imageUrl, EvidenceType activityType, UUID draftId);

    void createReadingEvidence(String title, String author, int page, String content, UUID draftId);

    void createOtherEvidence(String categoryName, MultipartFile file);

    void updateReviewStatus(Long evidenceId, ReviewStatus reviewStatus);

    void createOtherScoringEvidence(String categoryName, MultipartFile file, int value);

    void updateOtherScoringEvidenceByCurrentUser(Long evidenceId, MultipartFile file, int value, String imageUrl);

    CreateDraftEvidenceResponse createDraftActivityEvidence(UUID draftId, String categoryName, String title, String content, MultipartFile file, String imageUrl, EvidenceType activityType);

    CreateDraftEvidenceResponse createDraftReadingEvidence(UUID draftId, String title, String author, Integer page, String content);

    GetDraftActivityEvidenceResponse findDraftActivityEvidenceByDraftId(UUID draftId);

    GetDraftReadingEvidenceResponse findDraftReadingEvidenceByDraftId(UUID draftId);

    GetDraftEvidenceResponse findDraftEvidenceByCurrentUser();
}
