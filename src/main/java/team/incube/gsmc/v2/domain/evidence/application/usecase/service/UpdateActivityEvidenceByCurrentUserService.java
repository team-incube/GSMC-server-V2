package team.incube.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.incube.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.application.port.DiscordPort;
import team.incube.gsmc.v2.domain.evidence.application.port.EvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incube.gsmc.v2.domain.evidence.application.usecase.UpdateActivityEvidenceByCurrentUserUseCase;
import team.incube.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incube.gsmc.v2.domain.evidence.domain.Evidence;
import team.incube.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incube.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incube.gsmc.v2.global.event.FileUploadEvent;
import team.incube.gsmc.v2.global.event.ScoreUpdatedEvent;
import team.incube.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;
import team.incube.gsmc.v2.global.thirdparty.aws.exception.S3UploadFailedException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 활동 증빙자료 수정을 처리하는 유스케이스 구현 클래스입니다.
 * <p>{@link UpdateActivityEvidenceByCurrentUserUseCase}를 구현하며,
 * 제목, 내용, 이미지 URL, 증빙자료 유형 등을 기반으로 기존 활동 증빙자료를 수정합니다.
 * <p>파일이 새로 첨부되거나 이미지 URL이 변경된 경우, 기존 이미지는 즉시 삭제되며, 새 파일은 이벤트 기반 업로드됩니다.
 * <p>수정 시 검토 상태는 항상 {@code PENDING}으로 초기화됩니다.
 * @author suuuuuuminnnnnn
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UpdateActivityEvidenceByCurrentUserService implements UpdateActivityEvidenceByCurrentUserUseCase {

    private final EvidencePersistencePort evidencePersistencePort;
    private final ActivityEvidencePersistencePort activityEvidencePersistencePort;
    private final S3Port s3Port;
    private final CurrentMemberProvider currentMemberProvider;
    private final DiscordPort discordPort;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void execute(Long evidenceId, String title, String content, MultipartFile file, EvidenceType evidenceType) {
        Evidence evidence = evidencePersistencePort.findEvidenceByIdWithLock(evidenceId);
        ActivityEvidence activityEvidence = activityEvidencePersistencePort.findActivityEvidenceById(evidenceId);
        String email = currentMemberProvider.getCurrentUser().getEmail();

        String fileUri = deleteAndUploadFile(activityEvidence, file, evidence.getEvidenceType(), email, evidenceId);

        Evidence newEvidence = createEvidence(evidence, evidenceType);
        ActivityEvidence newActivityEvidence = createActivityEvidence(newEvidence, title, content, fileUri);

        activityEvidencePersistencePort.saveActivityEvidence(newActivityEvidence);

        applicationEventPublisher.publishEvent(new ScoreUpdatedEvent(email));
    }

    private String deleteAndUploadFile(ActivityEvidence activityEvidence, MultipartFile file, EvidenceType evidenceType, String email, Long evidenceId) {
        if (file != null && !file.isEmpty()) {
            s3Port.deleteFile(activityEvidence.getImageUrl());

            try {
                applicationEventPublisher.publishEvent(new FileUploadEvent(
                        evidenceId,
                        file.getOriginalFilename(),
                        file.getInputStream(),
                        evidenceType,
                        email
                ));
            } catch (IOException e) {
                discordPort.sendEvidenceUploadFailureAlert(evidenceId, file.getOriginalFilename(), email, e);
                throw new S3UploadFailedException();
            }
            return "upload_" + file.getOriginalFilename() + "_" + UUID.randomUUID();
        } else {
            return null;
        }
    }


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

    private ActivityEvidence createActivityEvidence(Evidence evidence, String title, String content, String fileUrl) {
        return ActivityEvidence.builder()
                .id(evidence)
                .title(title)
                .content(content)
                .imageUrl(fileUrl)
                .build();
    }
}
