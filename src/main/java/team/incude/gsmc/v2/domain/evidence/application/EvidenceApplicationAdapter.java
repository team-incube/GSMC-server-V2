package team.incude.gsmc.v2.domain.evidence.application;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.application.port.EvidenceApplicationPort;
import team.incude.gsmc.v2.domain.evidence.application.usecase.*;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.CreateDraftEvidenceResponse;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetActivityEvidenceResponse;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetEvidencesResponse;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetReadingEvidenceResponse;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.UUID;

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
    private final CreateOtherScoringEvidenceUseCase createOtherScoringUseCase;
    private final UpdateOtherScoringEvidenceByCurrentUserUseCase updateOtherScoringUseCase;
    private final CreateDraftActivityEvidenceUseCase createDraftActivityEvidenceUseCase;
    private final CreateDraftReadingEvidenceUseCase createDraftReadingEvidenceUseCase;


    @Override
    public GetEvidencesResponse findEvidenceByCurrentUserAndType(EvidenceType evidenceType) {
        return findEvidenceByCurrentUserAndTypeUseCase.execute(evidenceType);
    }

    @Override
    public GetEvidencesResponse findEvidenceByStudentCodeAndTypeAndStatus(String studentCode, EvidenceType evidenceType, ReviewStatus status) {
        return findEvidenceByStudentCodeAndFilteringTypeAndStatusUseCase.execute(studentCode, evidenceType, status);
    }

    @Override
    public GetEvidencesResponse findEvidenceByStudentCodeAndTitleAndType(String studentCode, String title, EvidenceType evidenceType) {
        return findEvidenceByFilteringByStudentCodeAndTitleAndTypeUseCase.execute(studentCode, title, evidenceType);
    }

    @Override
    public void updateMajorEvidenceByCurrentUser(Long evidenceId, String title, String content, MultipartFile file, String imageUrl) {
        updateActivityEvidenceByCurrentUserUseCase.execute(evidenceId, title, content, file, EvidenceType.MAJOR, imageUrl);
    }

    @Override
    public void updateHumanitiesEvidenceByCurrentUser(Long evidenceId, String title, String content, MultipartFile file, String imageUrl) {
        updateActivityEvidenceByCurrentUserUseCase.execute(evidenceId, title, content, file, EvidenceType.HUMANITIES, imageUrl);
    }

    @Override
    public void updateReadingEvidenceByCurrentUser(Long evidenceId, String title, String author, String content, int page) {
        updateReadingEvidenceByCurrentUserUseCase.execute(evidenceId, title, author, content, page);
    }

    @Override
    public void updateOtherEvidenceByCurrentUser(Long evidenceId, MultipartFile file, String imageUrl) {
        updateOtherEvidenceByCurrentUserUseCase.execute(evidenceId, file, imageUrl);
    }

    @Override
    public void deleteEvidence(Long evidenceId) {
        deleteEvidenceUseCase.execute(evidenceId);
    }

    @Override
    public void createActivityEvidence(String categoryName, String title, String content, MultipartFile file, String imageUrl, EvidenceType activityType, UUID draftId) {
        createActivityEvidenceUseCase.execute(categoryName, title, content, file, imageUrl, activityType, draftId);
    }

    @Override
    public void createReadingEvidence(String title, String author, int page, String content, UUID draftId) {
        createReadingEvidenceUseCase.execute(title, author, page, content, draftId);
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
    public void createOtherScoringEvidence(String categoryName, MultipartFile file, int value) {
        createOtherScoringUseCase.execute(categoryName, file, value);
    }

    @Override
    public void updateOtherScoringEvidenceByCurrentUser(Long evidenceId, MultipartFile file, int value, String imageUrl) {
        updateOtherScoringUseCase.execute(evidenceId, file, value, imageUrl);
    }

    @Override
    public CreateDraftEvidenceResponse createDraftActivityEvidence(UUID draftId, String categoryName, String title, String content, MultipartFile file, String imageUrl, EvidenceType activityType) {
        return createDraftActivityEvidenceUseCase.execute(draftId, categoryName, title, content, file, imageUrl, activityType);
    }

    @Override
    public CreateDraftEvidenceResponse createDraftReadingEvidence(UUID draftId, String title, String author, Integer page, String content) {
        return createDraftReadingEvidenceUseCase.execute(draftId, title, author, page, content);
    }

    @Override
    public GetActivityEvidenceResponse findDraftActivityEvidence(UUID draftId) {
        return null;
    }

    @Override
    public GetReadingEvidenceResponse findDraftReadingEvidence(UUID draftId) {
        return null;
    }
}
