package team.incude.gsmc.v2.domain.evidence.application;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.application.port.EvidenceApplicationPort;
import team.incude.gsmc.v2.domain.evidence.application.usecase.*;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.evidence.presentation.data.GetEvidencesResponse;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;

@Adapter(direction = PortDirection.INBOUND)
@RequiredArgsConstructor
public class EvidenceApplicationAdapter implements EvidenceApplicationPort {

    private final DeleteEvidenceUseCase deleteEvidenceUseCase;
    private final FindEvidenceByCurrentUserUseCase findEvidenceByCurrentUserUseCase;
    private final FindEvidenceByEmailUseCase findEvidenceByEmailUseCase;
    private final SubmitActivityEvidenceUseCase submitActivityEvidenceUseCase;
    private final SubmitOtherEvidenceUseCase submitOtherEvidenceUseCase;
    private final SubmitReadingEvidenceUseCase submitReadingEvidenceUseCase;
    private final UpdateHumanitiesEvidenceByCurrentUserUseCase updateHumanitiesEvidenceByCurrentUserUseCase;
    private final UpdateMajorEvidenceByCurrentUserUseCase updateMajorEvidenceByCurrentUserUseCase;
    private final UpdateOtherEvidenceByCurrentUserUseCase updateOtherEvidenceByCurrentUserUseCase;
    private final UpdateReadingEvidenceByCurrentUserUseCase updateReadingEvidenceByCurrentUserUseCase;
    private final UpdateReviewStatusUseCase updateReviewStatusUseCase;

    @Override
    public List<GetEvidencesResponse> findEvidenceByCurrentUser() {
        return findEvidenceByCurrentUserUseCase.execute();
    }

    @Override
    public List<GetEvidencesResponse> findEvidenceByEmail(String email) {
        return findEvidenceByEmailUseCase.execute(email);
    }

    @Override
    public void updateMajorEvidenceByCurrentUser(Long evidenceId, String title, String content, MultipartFile file) {
        updateMajorEvidenceByCurrentUserUseCase.execute(evidenceId, title, content, file);
    }

    @Override
    public void updateHumanitiesEvidenceByCurrentUser(Long evidenceId, String title, String content, MultipartFile file) {
        updateHumanitiesEvidenceByCurrentUserUseCase.execute(evidenceId, title, content, file);
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
    public void submitActivityEvidence(String categoryName, String title, String content, MultipartFile file, EvidenceType activityType) {
        submitActivityEvidenceUseCase.execute(categoryName, title, content, file, activityType);
    }

    @Override
    public void submitReadingEvidence(String title, String author, int page, String content) {
        submitReadingEvidenceUseCase.execute(title, author, page, content);
    }

    @Override
    public void submitOtherEvidence(String categoryName, MultipartFile file) {
        submitOtherEvidenceUseCase.execute(categoryName, file);
    }

    @Override
    public void updateReviewStatus(Long evidenceId, ReviewStatus reviewStatus) {
        updateReviewStatusUseCase.execute(evidenceId, reviewStatus);
    }
}
