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

    private final CreateActivityEvidenceUseCase createActivityEvidenceUseCase;
    private final CreateOtherEvidenceUseCase createOtherEvidenceUseCase;
    private final CreateReadingEvidenceUseCase createReadingEvidenceUseCase;
    private final DeleteEvidenceUseCase deleteEvidenceUseCase;
    private final FindEvidenceByCurrentUserAndTypeUseCase findEvidenceByCurrentUserAndTypeUseCase;
    private final FindEvidenceByFilteringByStudentCodeAndTitleAndTypeUseCase findEvidenceByFilteringByStudentCodeAndTitleAndTypeUseCase;
    private final FindEvidenceByStudentCodeAndFilteringTypeAndStatusUseCase findEvidenceByStudentCodeAndFilteringTypeAndStatusUseCase;
    private final UpdateActivityEvidenceByCurrentUserUseCase updateActivityEvidenceByCurrentUserUseCase;
    private final UpdateOtherEvidenceByCurrentUserUseCase updateOtherEvidenceByCurrentUserUseCase;
    private final UpdateReadingEvidenceByCurrentUserUseCase updateReadingEvidenceByCurrentUserUseCase;
    private final UpdateReviewStatusUseCase updateReviewStatusUseCase;

    @Override
    public GetEvidencesResponse findEvidenceByCurrentUserAndType(EvidenceType evidenceType) {
        return findEvidenceByCurrentUserAndTypeUseCase.execute(evidenceType);
    }

    @Override
    public GetEvidencesResponse findEvidenceByStudentCodeAndTypeAndStatus(Integer studentCode, EvidenceType evidenceType, ReviewStatus status) {
        return findEvidenceByStudentCodeAndFilteringTypeAndStatusUseCase.execute(studentCode, evidenceType, status);
    }

    @Override
    public GetEvidencesResponse findEvidenceByStudentCodeAndTitleAndType(Integer studentCode, String title, EvidenceType evidenceType) {
        return findEvidenceByFilteringByStudentCodeAndTitleAndTypeUseCase.execute(studentCode, title, evidenceType);
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
}
