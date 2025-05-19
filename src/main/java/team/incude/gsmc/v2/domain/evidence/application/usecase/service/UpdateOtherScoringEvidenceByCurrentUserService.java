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
import team.incude.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;
import team.incude.gsmc.v2.global.thirdparty.aws.exception.S3UploadFailedException;

import java.io.IOException;

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

    private Score createScore(Score score, Member member, int value) {
        return Score.builder()
                .id(score.getId())
                .member(member)
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
