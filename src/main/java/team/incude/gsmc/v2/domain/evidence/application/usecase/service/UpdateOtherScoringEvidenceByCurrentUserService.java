package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.application.port.DiscordPort;
import team.incude.gsmc.v2.domain.evidence.application.port.EvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incude.gsmc.v2.domain.evidence.application.usecase.UpdateOtherScoringEvidenceByCurrentUserUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.global.event.FileUploadEvent;
import team.incude.gsmc.v2.global.event.ScoreUpdatedEvent;
import team.incude.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;
import team.incude.gsmc.v2.global.thirdparty.aws.exception.S3UploadFailedException;

import java.io.IOException;
import java.util.UUID;

/**
 * 점수 기반 기타 증빙자료를 수정하는 유스케이스 구현 클래스입니다.
 * <p>{@link UpdateOtherScoringEvidenceByCurrentUserUseCase}를 구현하며,
 * 파일 및 점수 값을 수정하고 관련 정보를 갱신합니다.
 * <p>파일이 변경되면 기존 파일은 삭제되고 새 파일이 업로드되며,
 * 점수 값도 새로 설정됩니다. 검토 상태는 {@link ReviewStatus#PENDING}으로 초기화됩니다.
 * <p>수정 후 {@link ScoreUpdatedEvent}를 발행하여 점수 변경 사항을 알립니다.
 * @author suuuuuuminnnnnn
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UpdateOtherScoringEvidenceByCurrentUserService implements UpdateOtherScoringEvidenceByCurrentUserUseCase {

    private final EvidencePersistencePort evidencePersistencePort;
    private final ScorePersistencePort scorePersistencePort;
    private final OtherEvidencePersistencePort otherEvidencePersistencePort;
    private final CurrentMemberProvider currentMemberProvider;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final DiscordPort discordPort;
    private final S3Port s3Port;

    /**
     * 점수 기반 기타 증빙자료를 수정합니다.
     * <p>기존 Evidence 및 OtherEvidence, Score 정보를 불러와서
     * 파일/이미지 URL, 점수 값 등을 갱신하고 저장합니다.
     * 점수 변경 사항을 알리는 이벤트를 발행합니다.
     * @param evidenceId 수정할 증빙자료 ID
     * @param file 새로 첨부할 파일 (선택)
     * @param value 새로운 점수 값
     */
    @Override
    public void execute(Long evidenceId, MultipartFile file, int value) {
        Evidence evidence = evidencePersistencePort.findEvidenceByIdWithLock(evidenceId);
        OtherEvidence otherEvidence = otherEvidencePersistencePort.findOtherEvidenceById(evidenceId);
        String email = currentMemberProvider.getCurrentUser().getEmail();

        String fileUri = deleteAndUploadFile(otherEvidence, file, evidence.getId());

        Evidence newEvidence = createEvidence(evidence);
        Score newScore = createScore(evidence.getScore(), value);
        OtherEvidence newOtherEvidence = createOtherEvidence(newEvidence, fileUri);

        scorePersistencePort.saveScore(newScore);
        otherEvidencePersistencePort.saveOtherEvidence(newOtherEvidence);

        applicationEventPublisher.publishEvent(new ScoreUpdatedEvent(email));
    }

    /**
     * 기존 파일 URI와 요청된 이미지 URL을 비교하여 변경 여부를 판단합니다.
     * <p>변경되었다면 기존 파일을 삭제하고, 새 파일이 존재할 경우 업로드 이벤트를 발행합니다.
     * @param otherEvidence 기존 기타 증빙자료
     * @param file 새 파일
     * @return 최종적으로 저장할 파일 URI (업로드 예정 이름)
     */
    private String deleteAndUploadFile(OtherEvidence otherEvidence, MultipartFile file, Long evidenceId) {
        s3Port.deleteFile(otherEvidence.getFileUri());

        try {
            applicationEventPublisher.publishEvent(new FileUploadEvent(
                    evidenceId,
                    file.getOriginalFilename(),
                    file.getInputStream(),
                    otherEvidence.getId().getEvidenceType(),
                    currentMemberProvider.getCurrentUser().getEmail()
            ));
        } catch (IOException e) {
            discordPort.sendEvidenceUploadFailureAlert(
                    otherEvidence.getId().getId(),
                    file.getOriginalFilename(),
                    currentMemberProvider.getCurrentUser().getEmail(),
                    e
            );
            throw new S3UploadFailedException();
        }
        return "upload_" + file.getOriginalFilename() + "_" + UUID.randomUUID();
    }

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

    private Score createScore(Score score, int value) {
        return Score.builder()
                .id(score.getId())
                .member(score.getMember())
                .value(value)
                .category(score.getCategory())
                .build();
    }

    private OtherEvidence createOtherEvidence(Evidence evidence, String fileUrl) {
        return OtherEvidence.builder()
                .id(evidence)
                .fileUri(fileUrl)
                .build();
    }
}