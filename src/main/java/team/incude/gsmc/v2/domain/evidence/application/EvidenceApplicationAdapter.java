package team.incude.gsmc.v2.domain.evidence.application;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.application.port.EvidenceApplicationPort;
import team.incude.gsmc.v2.domain.evidence.application.usecase.DeleteEvidenceUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetEvidencesResponse;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;

@Adapter(direction = PortDirection.INBOUND)
@RequiredArgsConstructor
public class EvidenceApplicationAdapter implements EvidenceApplicationPort {

    // TODO: 추후 useCase 구현시 추가 예정

    private final DeleteEvidenceUseCase deleteEvidenceUseCase;

    @Override
    public List<GetEvidencesResponse> findEvidenceByCurrentUser() {
        return List.of();
    }

    @Override
    public List<GetEvidencesResponse> findEvidenceByEmail(String email) {
        return List.of();
    }

    @Override
    public void updateMajorEvidenceByCurrentUser(Long evidenceId, String title, String content, MultipartFile file) {

    }

    @Override
    public void updateHumanitiesEvidenceByCurrentUser(Long evidenceId, String title, String content, MultipartFile file) {

    }

    @Override
    public void updateReadingEvidenceByCurrentUser(Long evidenceId, String title, String author, String content, int page) {

    }

    @Override
    public void updateOtherEvidenceByCurrentUser(Long evidenceId, MultipartFile file) {

    }

    @Override
    public void deleteEvidence(Long evidenceId) {
        deleteEvidenceUseCase.execute(evidenceId);
    }

    @Override
    public void createActivityEvidence(String categoryName, String title, String content, MultipartFile file, EvidenceType activityType) {

    }

    @Override
    public void createReadingEvidence(String title, String author, int page, String content) {

    }

    @Override
    public void createOtherEvidence(String categoryName, MultipartFile file) {

    }

    @Override
    public void updateReviewStatus(Long evidenceId, ReviewStatus reviewStatus) {

    }
}
