package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incude.gsmc.v2.domain.evidence.application.usecase.CreateOtherEvidenceUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
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
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateOtherEvidenceService implements CreateOtherEvidenceUseCase {

    private final S3Port s3Port;
    private final StudentDetailPersistencePort studentDetailPersistencePort;
    private final OtherEvidencePersistencePort otherEvidencePersistencePort;
    private final ScorePersistencePort scorePersistencePort;
    private final CurrentMemberProvider currentMemberProvider;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public void execute(String categoryName, MultipartFile file) {
        Member member = currentMemberProvider.getCurrentUser();
        StudentDetail studentDetail = studentDetailPersistencePort.findStudentDetailByMemberEmail(member.getEmail());
        Score score = scorePersistencePort.findScoreByCategoryNameAndMemberEmail(categoryName, member.getEmail());
        score.plusValue(1);

        Evidence evidence = createEvidence(score);
        String fileUrl = uploadFile(file);
        OtherEvidence otherEvidence = createOtherEvidence(evidence, fileUrl);

        scorePersistencePort.saveScore(score);
        otherEvidencePersistencePort.saveOtherEvidence(otherEvidence);
        applicationEventPublisher.publishEvent(new ScoreUpdatedEvent(studentDetail.getStudentCode()));
    }

    private Evidence createEvidence(Score score) {
        return Evidence.builder()
                .score(score)
                .reviewStatus(ReviewStatus.PENDING)
                .evidenceType(EvidenceType.READ_A_THON)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
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
