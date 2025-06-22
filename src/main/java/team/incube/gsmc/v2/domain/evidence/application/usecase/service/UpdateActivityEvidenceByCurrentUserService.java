package team.incube.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.incube.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.application.port.EvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incube.gsmc.v2.domain.evidence.application.usecase.UpdateActivityEvidenceByCurrentUserUseCase;
import team.incube.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incube.gsmc.v2.domain.evidence.domain.Evidence;
import team.incube.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incube.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incube.gsmc.v2.global.thirdparty.aws.exception.S3UploadFailedException;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 활동 증빙자료 수정을 처리하는 유스케이스 구현 클래스입니다.
 * <p>{@link UpdateActivityEvidenceByCurrentUserUseCase}를 구현하며,
 * 제목, 내용, 이미지 URL, 증빙자료 유형 등을 기반으로 기존 활동 증빙자료를 수정합니다.
 * <p>파일이 새로 첨부되거나 이미지 URL이 변경된 경우, 기존 이미지를 S3에서 삭제하고 새 파일을 업로드합니다.
 * <p>수정 시 검토 상태는 항상 {@code PENDING}으로 초기화됩니다.
 * @author suuuuuuminnnnnn
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UpdateActivityEvidenceByCurrentUserService implements UpdateActivityEvidenceByCurrentUserUseCase {

    private final EvidencePersistencePort evidencePersistencePort;
    private final S3Port s3Port;
    private final ActivityEvidencePersistencePort activityEvidencePersistencePort;

    /**
     * 활동 증빙자료를 수정합니다.
     * <p>기존 증빙자료를 불러온 후 제목, 내용, 이미지 URL, 타입을 반영하여 수정합니다.
     * <p>새 이미지가 첨부되면 기존 이미지는 삭제되며, 검토 상태는 {@code PENDING}으로 초기화됩니다.
     * @param evidenceId 수정할 증빙자료 ID
     * @param title 활동 제목
     * @param content 활동 내용
     * @param file 새 첨부 파일 (선택)
     * @param evidenceType 증빙자료 유형
     * @param imageUrl 이미지 URL (선택)
     */
    @Override
    public void execute(Long evidenceId, String title, String content, MultipartFile file, EvidenceType evidenceType, String imageUrl) {
        Evidence evidence = evidencePersistencePort.findEvidenceByIdWithLock(evidenceId);
        ActivityEvidence activityEvidence = activityEvidencePersistencePort.findActivityEvidenceById(evidenceId);

        String fileUrl = checkImageUrl(activityEvidence, imageUrl, file);

        Evidence newEvidence = createEvidence(evidence, evidenceType);
        ActivityEvidence newActivityEvidence = createActivityEvidence(newEvidence, title, content, fileUrl);

        activityEvidencePersistencePort.saveActivityEvidence(newActivityEvidence);
    }

    /**
     * 이미지 변경 여부를 판단하고 필요한 경우 기존 이미지를 삭제한 뒤 새 파일을 업로드합니다.
     * @param activityEvidence 기존 활동 증빙자료
     * @param imageUrl 요청된 이미지 URL
     * @param file 업로드할 새 파일
     * @return 최종 이미지 URL 또는 null
     */
    private String checkImageUrl(ActivityEvidence activityEvidence, String imageUrl, MultipartFile file) {
        if (imageUrl != null
                && !imageUrl.isEmpty()
                && activityEvidence.getImageUrl().equals(imageUrl)) {
            return imageUrl;
        }

        s3Port.deleteFile(activityEvidence.getImageUrl());

        if (file != null && !file.isEmpty()){
            return uploadFile(file);
        } else {
            return null;
        }
    }

    /**
     * 수정된 값으로 새로운 {@link ActivityEvidence} 객체를 생성합니다.
     * @param evidence 연관 Evidence 객체
     * @param title 제목
     * @param content 내용
     * @param fileUrl 이미지 URL
     * @return 새로운 ActivityEvidence 객체
     */
    private ActivityEvidence createActivityEvidence(Evidence evidence, String title, String content, String fileUrl) {
        return ActivityEvidence.builder()
                .id(evidence)
                .title(title)
                .content(content)
                .imageUrl(fileUrl)
                .build();
    }

    /**
     * 수정 사항을 반영하여 새로운 {@link Evidence} 객체를 생성합니다.
     * <p>검토 상태는 항상 {@code PENDING}으로 초기화됩니다.
     * @param evidence 기존 Evidence
     * @param evidenceType 수정할 증빙자료 유형
     * @return 수정된 Evidence 객체
     */
    private Evidence createEvidence(Evidence evidence, EvidenceType evidenceType) {
        return Evidence.builder()
                .id(evidence.getId())
                .score(evidence.getScore())
                .reviewStatus(ReviewStatus.PENDING)
                .evidenceType(evidenceType)
                .createdAt(evidence.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * MultipartFile을 S3에 업로드하고 URL을 반환합니다.
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