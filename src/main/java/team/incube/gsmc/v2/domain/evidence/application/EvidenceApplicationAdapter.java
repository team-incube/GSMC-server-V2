package team.incube.gsmc.v2.domain.evidence.application;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import team.incube.gsmc.v2.domain.evidence.application.port.EvidenceApplicationPort;
import team.incube.gsmc.v2.domain.evidence.application.usecase.*;
import team.incube.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incube.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incube.gsmc.v2.domain.evidence.presentation.data.response.*;
import team.incube.gsmc.v2.global.annotation.PortDirection;
import team.incube.gsmc.v2.global.annotation.adapter.Adapter;

import java.io.InputStream;
import java.util.UUID;

/**
 * 증빙자료 도메인 기능을 실행하는 애플리케이션 어댑터 클래스입니다.
 * <p>{@link EvidenceApplicationPort}를 구현하며, Web 어댑터로부터 전달된 요청을 실제 유스케이스 실행으로 위임합니다.
 * <p>활동, 독서, 기타, 점수 기반 기타 증빙자료의 생성, 수정, 삭제, 상태 변경 및 임시저장 관련 기능을 처리합니다.
 * @author suuuuuuminnnnnn
 */
@Adapter(direction = PortDirection.INBOUND)
@RequiredArgsConstructor
public class EvidenceApplicationAdapter implements EvidenceApplicationPort {

    private final CreateActivityEvidenceUseCase createActivityEvidenceUseCase;
    private final CreateOtherEvidenceUseCase createOtherEvidenceUseCase;
    private final CreateReadingEvidenceUseCase createReadingEvidenceUseCase;
    private final DeleteEvidenceUseCase deleteEvidenceUseCase;
    private final FindEvidenceByCurrentUserAndTypeUseCase findEvidenceByCurrentUserAndTypeUseCase;
    private final FindEvidenceByTitleAndTypeUseCase findEvidenceByTitleAndTypeUseCase;
    private final FindEvidenceByEmailAndTypeAndStatusUseCase findEvidenceByEmailAndTypeAndStatusUseCase;
    private final UpdateActivityEvidenceByCurrentUserUseCase updateActivityEvidenceByCurrentUserUseCase;
    private final UpdateOtherEvidenceByCurrentUserUseCase updateOtherEvidenceByCurrentUserUseCase;
    private final UpdateReadingEvidenceByCurrentUserUseCase updateReadingEvidenceByCurrentUserUseCase;
    private final UpdateReviewStatusUseCase updateReviewStatusUseCase;
    private final CreateOtherScoringEvidenceUseCase createOtherScoringUseCase;
    private final UpdateOtherScoringEvidenceByCurrentUserUseCase updateOtherScoringUseCase;
    private final CreateDraftActivityEvidenceUseCase createDraftActivityEvidenceUseCase;
    private final CreateDraftReadingEvidenceUseCase createDraftReadingEvidenceUseCase;
    private final FindDraftActivityEvidenceByDraftIdUseCase findDraftActivityEvidenceUseCase;
    private final FindDraftReadingEvidenceByDraftIdUseCase findDraftReadingEvidenceUseCase;
    private final FindDraftEvidenceByCurrentUserUseCase findDraftEvidenceByCurrentUserUseCase;
    private final UpdateEvidenceFileUseCase updateEvidenceFileUseCase;

    /**
     * 현재 사용자의 증빙자료를 타입별로 조회합니다.
     */
    @Override
    public GetEvidencesResponse findEvidenceByCurrentUserAndType(EvidenceType evidenceType) {
        return findEvidenceByCurrentUserAndTypeUseCase.execute(evidenceType);
    }

    /**
     * 학생코드 + 증빙자료 타입 + 상태 조건으로 증빙자료를 조회합니다.
     */
    @Override
    public GetEvidencesResponse findEvidenceByEmailAndTypeAndStatus(String email, EvidenceType evidenceType, ReviewStatus status) {
        return findEvidenceByEmailAndTypeAndStatusUseCase.execute(email, evidenceType, status);
    }

    /**
     * 제목 + 증빙자료 타입 조건으로 증빙자료를 조회합니다.
     */
    @Override
    public GetEvidencesResponse findEvidenceByTitleAndType(String title, EvidenceType evidenceType) {
        return findEvidenceByTitleAndTypeUseCase.execute(title, evidenceType);
    }

    /**
     * 전공 활동 증빙자료를 수정합니다.
     */
    @Override
    public void updateMajorEvidenceByCurrentUser(Long evidenceId, String title, String content, MultipartFile file, String imageUrl) {
        updateActivityEvidenceByCurrentUserUseCase.execute(evidenceId, title, content, file, EvidenceType.MAJOR, imageUrl);
    }

    /**
     * 인문 활동 증빙자료를 수정합니다.
     */
    @Override
    public void updateHumanitiesEvidenceByCurrentUser(Long evidenceId, String title, String content, MultipartFile file, String imageUrl) {
        updateActivityEvidenceByCurrentUserUseCase.execute(evidenceId, title, content, file, EvidenceType.HUMANITIES, imageUrl);
    }

    /**
     * 독서 증빙자료를 수정합니다.
     */
    @Override
    public void updateReadingEvidenceByCurrentUser(Long evidenceId, String title, String author, String content, int page) {
        updateReadingEvidenceByCurrentUserUseCase.execute(evidenceId, title, author, content, page);
    }

    /**
     * 기타 증빙자료를 수정합니다.
     */
    @Override
    public void updateOtherEvidenceByCurrentUser(Long evidenceId, MultipartFile file, String imageUrl) {
        updateOtherEvidenceByCurrentUserUseCase.execute(evidenceId, file, imageUrl);
    }

    /**
     * 증빙자료를 삭제합니다.
     */
    @Override
    public void deleteEvidence(Long evidenceId) {
        deleteEvidenceUseCase.execute(evidenceId);
    }

    /**
     * 활동 증빙자료를 생성합니다.
     */
    @Override
    public void createActivityEvidence(String categoryName, String title, String content, MultipartFile file, String imageUrl, EvidenceType activityType, UUID draftId) {
        createActivityEvidenceUseCase.execute(categoryName, title, content, file, imageUrl, activityType, draftId);
    }

    /**
     * 독서 증빙자료를 생성합니다.
     */
    @Override
    public void createReadingEvidence(String title, String author, int page, String content, UUID draftId) {
        createReadingEvidenceUseCase.execute(title, author, page, content, draftId);
    }

    /**
     * 기타 증빙자료를 생성합니다.
     */
    @Override
    public void createOtherEvidence(String categoryName, MultipartFile file) {
        createOtherEvidenceUseCase.execute(categoryName, file);
    }

    /**
     * 증빙자료의 검토 상태를 변경합니다.
     */
    @Override
    public void updateReviewStatus(Long evidenceId, ReviewStatus reviewStatus) {
        updateReviewStatusUseCase.execute(evidenceId, reviewStatus);
    }

    /**
     * 점수 기반 기타 증빙자료를 생성합니다.
     */
    @Override
    public void createOtherScoringEvidence(String categoryName, MultipartFile file, int value) {
        createOtherScoringUseCase.execute(categoryName, file, value);
    }

    /**
     * 점수 기반 기타 증빙자료를 수정합니다.
     */
    @Override
    public void updateOtherScoringEvidenceByCurrentUser(Long evidenceId, MultipartFile file, int value, String imageUrl) {
        updateOtherScoringUseCase.execute(evidenceId, file, value, imageUrl);
    }

    /**
     * 활동 증빙자료 임시저장을 생성하거나 갱신합니다.
     */
    @Override
    public CreateDraftEvidenceResponse createDraftActivityEvidence(UUID draftId, String categoryName, String title, String content, MultipartFile file, String imageUrl, EvidenceType activityType) {
        return createDraftActivityEvidenceUseCase.execute(draftId, categoryName, title, content, file, imageUrl, activityType);
    }

    /**
     * 독서 증빙자료 임시저장을 생성하거나 갱신합니다.
     */
    @Override
    public CreateDraftEvidenceResponse createDraftReadingEvidence(UUID draftId, String title, String author, Integer page, String content) {
        return createDraftReadingEvidenceUseCase.execute(draftId, title, author, page, content);
    }

    /**
     * 활동 증빙자료 임시저장을 ID로 조회합니다.
     */
    @Override
    public GetDraftActivityEvidenceResponse findDraftActivityEvidenceByDraftId(UUID draftId) {
        return findDraftActivityEvidenceUseCase.execute(draftId);
    }

    /**
     * 독서 증빙자료 임시저장을 ID로 조회합니다.
     */
    @Override
    public GetDraftReadingEvidenceResponse findDraftReadingEvidenceByDraftId(UUID draftId) {
        return findDraftReadingEvidenceUseCase.execute(draftId);
    }

    /**
     * 현재 사용자의 전체 임시저장 증빙자료를 조회합니다.
     */
    @Override
    public GetDraftEvidenceResponse findDraftEvidenceByCurrentUser() {
        return findDraftEvidenceByCurrentUserUseCase.execute();
    }

    @Override
    public void updateEvidenceFile(Long evidenceId, String fileName, InputStream inputStream, EvidenceType evidenceType) {
        updateEvidenceFileUseCase.execute(evidenceId, fileName, inputStream, evidenceType);
    }
}