package team.incude.gsmc.v2.domain.evidence.application.port;

import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.CreateDraftEvidenceResponse;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetActivityEvidenceResponse;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetEvidencesResponse;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetReadingEvidenceResponse;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.util.UUID;

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

    GetActivityEvidenceResponse findDraftActivityEvidence(UUID draftId);

    GetReadingEvidenceResponse findDraftReadingEvidence(UUID draftId);
}
