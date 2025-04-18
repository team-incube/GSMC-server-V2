package team.incude.gsmc.v2.domain.evidence.application.port;

import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetEvidencesResponse;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

@Port(direction = PortDirection.INBOUND)
public interface EvidenceApplicationPort {
    GetEvidencesResponse findEvidenceByCurrentUserAndType(EvidenceType evidenceType);

    GetEvidencesResponse findEvidenceByEmailAndType(String email, EvidenceType evidenceType);

    GetEvidencesResponse findEvidenceByEmailAndTitleAndType(String email, String title, EvidenceType evidenceType);

    void updateMajorEvidenceByCurrentUser(Long evidenceId, String title, String content, MultipartFile file);

    void updateHumanitiesEvidenceByCurrentUser(Long evidenceId, String title, String content, MultipartFile file);

    void updateReadingEvidenceByCurrentUser(Long evidenceId, String title, String author, String content, int page);

    void updateOtherEvidenceByCurrentUser(Long evidenceId, MultipartFile file);

    void deleteEvidence(Long evidenceId);

    void createActivityEvidence(String categoryName, String title, String content, MultipartFile file, EvidenceType activityType);

    void createReadingEvidence(String title, String author, int page, String content);

    void createOtherEvidence(String categoryName, MultipartFile file);

    void updateReviewStatus(Long evidenceId, ReviewStatus reviewStatus);
}
