package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.application.port.DraftActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incude.gsmc.v2.domain.evidence.application.usecase.CreateDraftActivityEvidenceUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.DraftActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.CreateDraftEvidenceResponse;
import team.incude.gsmc.v2.global.thirdparty.aws.exception.S3UploadFailedException;

import java.io.IOException;
import java.util.UUID;

/**
 * 활동 증빙자료 임시저장을 처리하는 유스케이스 구현 클래스입니다.
 *
 * <p>{@link CreateDraftActivityEvidenceUseCase}를 구현하며, 증빙자료 작성 중간 데이터를 Redis 등에 임시 저장합니다.
 * <p>제목, 내용, 파일 또는 이미지 URL, 활동 유형 등을 포함하여 저장되며,
 * {@code id}가 null이면 새 UUID를 생성하고, 존재하면 기존 임시저장을 갱신합니다.
 * <p>저장 시 TTL(7일)이 설정되며, 이후 자동 삭제됩니다.
 * @author suuuuuuminnnnnn
 */
@Service
@RequiredArgsConstructor
public class CreateDraftActivityEvidenceService implements CreateDraftActivityEvidenceUseCase {

    private static final Long DRAFT_TTL_SECONDS = 7 * 24 * 60 * 60L; // 7일

    private final DraftActivityEvidencePersistencePort draftActivityEvidencePersistencePort;
    private final S3Port s3Port;

    /**
     * 활동 증빙자료를 임시저장합니다.
     * <p>{@code id}가 존재하면 해당 ID로 덮어쓰며, 존재하지 않으면 새 UUID를 생성하여 저장합니다.
     * @param id 기존 임시저장 ID (null 시 새로 생성)
     * @param categoryName 카테고리명
     * @param title 제목
     * @param content 내용
     * @param file 첨부 파일 (선택)
     * @param imageUrl 이미지 URL (선택)
     * @param activityType 활동 유형
     * @return 생성된 임시저장 ID
     */
    @Override
    public CreateDraftEvidenceResponse execute(UUID id, String categoryName, String title, String content, MultipartFile file, String imageUrl, EvidenceType activityType) {
        UUID draftId = id == null ? UUID.randomUUID() : id;

        DraftActivityEvidence draftActivityEvidence = createActivityEvidence(draftId, categoryName, title, content, file, imageUrl, activityType);

        draftActivityEvidencePersistencePort.saveDraftActivityEvidence(draftActivityEvidence);
        return new CreateDraftEvidenceResponse(draftId);
    }

    /**
     * DraftActivityEvidence 객체를 생성합니다.
     * <p>파일 또는 이미지 URL 중 하나가 존재할 경우 이미지 정보를 설정합니다.
     * @param id 임시저장 ID
     * @param categoryName 카테고리명
     * @param title 제목
     * @param content 내용
     * @param file 파일 (선택)
     * @param imageUrlParam 외부 이미지 URL (선택)
     * @param evidenceType 활동 유형
     * @return 생성된 DraftActivityEvidence 객체
     */
    private DraftActivityEvidence createActivityEvidence(UUID id, String categoryName, String title, String content, MultipartFile file, String imageUrlParam, EvidenceType evidenceType) {
        String imageUrl = null;

        if (file != null && !file.isEmpty()) {
            imageUrl = uploadFile(file);
        } else if (imageUrlParam != null && !imageUrlParam.isBlank()) {
            imageUrl = imageUrlParam;
        }

        return DraftActivityEvidence.builder()
                .id(id)
                .categoryName(categoryName)
                .title(title)
                .content(content)
                .imageUrl(imageUrl)
                .evidenceType(evidenceType)
                .ttl(DRAFT_TTL_SECONDS)
                .build();
    }

    /**
     * MultipartFile을 S3에 업로드하고 해당 URL을 반환합니다.
     * @param file 업로드할 파일
     * @return 업로드된 이미지 URL
     * @throws S3UploadFailedException 업로드 실패 시
     */
    private String uploadFile(MultipartFile file) {
        try {
            return s3Port.uploadFile(
                    file.getOriginalFilename(),
                    file.getInputStream()
            ).join();
        } catch (IOException e) {
            throw new S3UploadFailedException();
        }
    }
}