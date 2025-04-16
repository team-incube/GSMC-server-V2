package team.incude.gsmc.v2.domain.evidence.application;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.application.port.EvidenceApplicationPort;
import team.incude.gsmc.v2.domain.evidence.application.usecase.*;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetEvidencesResponse;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

@Adapter(direction = PortDirection.INBOUND)
@RequiredArgsConstructor
public class EvidenceApplicationAdapter implements EvidenceApplicationPort {

    private final DeleteEvidenceUseCase deleteEvidenceUseCase;
    private final FindEvidenceByCurrentUserAndTypeUseCase findEvidenceByCurrentUserAndTypeUseCase;
    private final UpdateActivityEvidenceByCurrentUserUseCase updateActivityEvidenceByCurrentUserUseCase;
    private final UpdateReadingEvidenceByCurrentUserUseCase updateReadingEvidenceByCurrentUserUseCase;
    private final UpdateOtherEvidenceByCurrentUserUseCase updateOtherEvidenceByCurrentUserUseCase;
    private final CreateActivityEvidenceUseCase createActivityEvidenceUseCase;
    private final CreateReadingEvidenceUseCase createReadingEvidenceUseCase;
    private final CreateOtherEvidenceUseCase createOtherEvidenceUseCase;
    private final UpdateReviewStatusUseCase updateReviewStatusUseCase;
    private final FindEvidenceByFilteringByEmailAndTitleAndTypeUseCase findEvidenceByFilteringByEmailAndTitleAndTypeUseCase;

    @Override
    public GetEvidencesResponse findEvidenceByCurrentUserAndType(EvidenceType type) {
        return findEvidenceByCurrentUserAndTypeUseCase.execute(type);
    }

    @Override
    public GetEvidencesResponse findEvidenceByEmailAndType(String email, EvidenceType type) {
        return findEvidenceByFilteringByEmailAndTitleAndTypeUseCase.execute(email, type);
    }

    @Override
    public void updateMajorEvidenceByCurrentUser(Long evidenceId, String title, String content, MultipartFile file) {
        updateActivityEvidenceByCurrentUserUseCase.execute(evidenceId, title, content, file, EvidenceType.MAJOR);
    }

    @Override
    public void updateHumanitiesEvidenceByCurrentUser(Long evidenceId, String title, String content, MultipartFile file) {
        updateActivityEvidenceByCurrentUserUseCase.execute(evidenceId, title, content, file, EvidenceType.HUMANITIES);
    }

    @Override
    public void updateReadingEvidenceByCurrentUser(Long evidenceId, String title, String author, String content, int page) {
        updateReadingEvidenceByCurrentUserUseCase.execute(evidenceId, title, author, content, page);
    }

    @Override
    public void updateOtherEvidenceByCurrentUser(Long evidenceId, MultipartFile file) {
        updateOtherEvidenceByCurrentUserUseCase.execute(evidenceId, file);
    }

    @Override
    public void deleteEvidence(Long evidenceId) {
        deleteEvidenceUseCase.execute(evidenceId);
    }

    @Override
    public void createActivityEvidence(String categoryName, String title, String content, MultipartFile file, EvidenceType activityType) {
        createActivityEvidenceUseCase.execute(categoryName, title, content, file, activityType);
    }

    @Override
    public void createReadingEvidence(String title, String author, int page, String content) {
        createReadingEvidenceUseCase.execute(title, author, page, content);
    }

    @Override
    public void createOtherEvidence(String categoryName, MultipartFile file) {
        createOtherEvidenceUseCase.execute(categoryName, file);
    }

    @Override
    public void updateReviewStatus(Long evidenceId, ReviewStatus reviewStatus) {
        updateReviewStatusUseCase.execute(evidenceId, reviewStatus);
    }

    @Override
    public GetEvidencesResponse findEvidenceByEmailAndTitleAndType(String email, String title, EvidenceType evidenceType) {
        return findEvidenceByFilteringByEmailAndTitleAndTypeUseCase.execute(email, title, evidenceType);
    }
}
