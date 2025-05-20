package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.application.port.EvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incude.gsmc.v2.domain.evidence.application.usecase.UpdateOtherScoringEvidenceByCurrentUserUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.member.domain.StudentDetail;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.global.event.ScoreUpdatedEvent;
import team.incude.gsmc.v2.global.security.jwt.usecase.service.CurrentMemberProvider;
import team.incude.gsmc.v2.global.thirdparty.aws.exception.S3UploadFailedException;

import java.io.IOException;

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
    private final S3Port s3Port;
    private final StudentDetailPersistencePort studentDetailPersistencePort;
    private final OtherEvidencePersistencePort otherEvidencePersistencePort;
    private final CurrentMemberProvider currentMemberProvider;
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 점수 기반 기타 증빙자료를 수정합니다.
     * <p>기존 Evidence 및 OtherEvidence, Score 정보를 불러와서
     * 파일/이미지 URL, 점수 값 등을 갱신하고 저장합니다.
     * 점수 변경 사항을 알리는 이벤트를 발행합니다.
     * @param evidenceId 수정할 증빙자료 ID
     * @param file 새로 첨부할 파일 (선택)
     * @param value 새로운 점수 값
     * @param imageUrl 대체할 이미지 URL (선택)
     */
    @Override
    public void execute(Long evidenceId, MultipartFile file, int value, String imageUrl) {
        Evidence evidence = evidencePersistencePort.findEvidenceByIdWithLock(evidenceId);
        Member member = currentMemberProvider.getCurrentUser();
        OtherEvidence otherEvidence = otherEvidencePersistencePort.findOtherEvidenceById(evidenceId);
        StudentDetail studentDetail = studentDetailPersistencePort.findStudentDetailByMemberEmail(member.getEmail());
        Score score = evidence.getScore();

        String fileUrl = checkImageUrl(otherEvidence, imageUrl, file);

        Evidence newEvidence = createEvidence(evidence);
        Score newScore = createScore(score, member, value);
        OtherEvidence newOtherEvidence = createOtherEvidence(newEvidence, fileUrl);

        scorePersistencePort.saveScore(newScore);
        otherEvidencePersistencePort.saveOtherEvidence(newOtherEvidence);

        applicationEventPublisher.publishEvent(new ScoreUpdatedEvent(studentDetail.getStudentCode()));
    }

    /**
     * 기존 파일 URI와 변경 요청된 이미지 URL을 비교하고, 필요 시 S3에서 기존 파일을 삭제합니다.
     * @param otherEvidence 기존 기타 증빙자료
     * @param imageUrl 요청된 이미지 URL
     * @param file 새 첨부 파일
     * @return 최종 저장할 이미지 URI
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
     * 수정된 상태를 반영하여 새로운 Evidence 객체를 생성합니다.
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
     * 점수 값을 갱신하여 새로운 Score 객체를 생성합니다.
     * @param score 기존 Score
     * @param member 사용자 정보
     * @param value 새로운 점수 값
     * @return 수정된 Score 객체
     */
    private Score createScore(Score score, Member member, int value) {
        return Score.builder()
                .id(score.getId())
                .member(member)
                .value(value)
                .category(score.getCategory())
                .build();
    }

    /**
     * 파일 URI를 기반으로 새로운 OtherEvidence 객체를 생성합니다.
     * @param evidence 연관 Evidence
     * @param fileUrl 이미지 URI
     * @return 생성된 OtherEvidence 객체
     */
    private OtherEvidence createOtherEvidence(Evidence evidence, String fileUrl) {
        return OtherEvidence.builder()
                .id(evidence)
                .fileUri(fileUrl)
                .build();
    }

    /**
     * MultipartFile을 S3에 업로드하고 URI를 반환합니다.
     * @param file 업로드할 파일
     * @return 업로드된 파일의 URI
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