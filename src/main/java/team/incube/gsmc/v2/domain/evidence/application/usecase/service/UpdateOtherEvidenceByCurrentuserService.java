package team.incube.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.incube.gsmc.v2.domain.evidence.application.port.EvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incube.gsmc.v2.domain.evidence.application.usecase.UpdateOtherEvidenceByCurrentUserUseCase;
import team.incube.gsmc.v2.domain.evidence.domain.Evidence;
import team.incube.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incube.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incube.gsmc.v2.global.thirdparty.aws.exception.S3UploadFailedException;

import java.io.IOException;


/**
 * 기타 증빙자료 수정을 처리하는 유스케이스 구현 클래스입니다.
 * <p>{@link UpdateOtherEvidenceByCurrentUserUseCase}를 구현하며,
 * 첨부된 이미지 URL 또는 파일을 수정하고, 기존 이미지를 삭제 및 교체합니다.
 * <p>수정 시 검토 상태는 {@code PENDING}으로 초기화되며,
 * 수정된 내용은 새 Evidence 및 OtherEvidence 객체로 저장됩니다.
 * @author suuuuuuminnnnnn
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UpdateOtherEvidenceByCurrentuserService implements UpdateOtherEvidenceByCurrentUserUseCase {

    private final OtherEvidencePersistencePort otherEvidencePersistencePort;
    private final EvidencePersistencePort evidencePersistencePort;
    private final S3Port s3Port;

    /**
     * 기타 증빙자료를 수정합니다.
     * <p>기존 Evidence 및 OtherEvidence를 조회한 후,
     * 파일 또는 이미지 URL을 기반으로 이미지를 교체하고, 검토 상태를 초기화한 후 새 객체로 저장합니다.
     * @param evidenceId 수정할 증빙자료 ID
     * @param file 새로 첨부할 파일 (선택)
     * @param imageUrl 대체할 이미지 URL (선택)
     */
    @Override
    public void execute(Long evidenceId, MultipartFile file, String imageUrl) {
        Evidence evidence = evidencePersistencePort.findEvidenceByIdWithLock(evidenceId);
        OtherEvidence otherEvidence = otherEvidencePersistencePort.findOtherEvidenceById(evidenceId);

        String fileUri = checkImageUrl(otherEvidence, imageUrl, file);

        Evidence newEvidence = createEvidence(evidence);
        OtherEvidence newOtherEvidence = createOtherEvidence(newEvidence, fileUri);
        otherEvidencePersistencePort.saveOtherEvidence(newOtherEvidence);
    }

    /**
     * 기존 파일 URI와 요청된 이미지 URL을 비교하여 변경 여부를 판단합니다.
     * <p>변경되었다면 기존 파일을 삭제하고, 새 파일이 존재할 경우 업로드합니다.
     * @param otherEvidence 기존 기타 증빙자료
     * @param imageUrl 새 이미지 URL
     * @param file 새 파일
     * @return 최종적으로 저장할 파일 URI
     */
    private String checkImageUrl(OtherEvidence otherEvidence, String imageUrl, MultipartFile file) {
        if (imageUrl != null
                && !imageUrl.isEmpty()
                && otherEvidence.getFileUri().equals(imageUrl)) {
            return imageUrl;
        }

        s3Port.deleteFile(otherEvidence.getFileUri());

        if (file != null && !file.isEmpty()){
            return uploadFile(file);
        } else {
            return null;
        }
    }

    /**
     * 기존 정보를 기반으로 검토 상태가 초기화된 새로운 Evidence 객체를 생성합니다.
     * @param evidence 기존 Evidence
     * @return 수정된 Evidence 객체
     */
    private Evidence createEvidence(Evidence evidence) {
        return Evidence.builder()
                .id(evidence.getId())
                .score(evidence.getScore())
                .reviewStatus(ReviewStatus.PENDING)
                .evidenceType(evidence.getEvidenceType())
                .createdAt(evidence.getCreatedAt())
                .updatedAt(evidence.getUpdatedAt())
                .build();
    }

    /**
     * Evidence와 파일 URI를 기반으로 새로운 OtherEvidence 객체를 생성합니다.
     * @param evidence 연관된 Evidence
     * @param fileUrl 저장할 파일 URI
     * @return 생성된 OtherEvidence 객체
     */
    private OtherEvidence createOtherEvidence(Evidence evidence, String fileUrl) {
        return OtherEvidence.builder()
                .id(evidence)
                .fileUri(fileUrl)
                .build();
    }

    /**
     * MultipartFile을 S3에 업로드하고 해당 URL을 반환합니다.
     * @param file 업로드할 파일
     * @return 업로드된 파일의 URL
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
