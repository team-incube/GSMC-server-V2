package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incude.gsmc.v2.domain.evidence.application.usecase.CreateActivityEvidenceUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
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
@Transactional
public class CreateActivityEvidenceService implements CreateActivityEvidenceUseCase {

    private final ActivityEvidencePersistencePort activityEvidencePersistencePort;
    private final ScorePersistencePort scorePersistencePort;
    private final StudentDetailPersistencePort studentDetailPersistencePort;
    private final S3Port s3Port;
    private final CurrentMemberProvider currentMemberProvider;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void execute(String categoryName, String title, String content, MultipartFile file, EvidenceType activityType) {
        Member member = currentMemberProvider.getCurrentUser();
        StudentDetail studentDetail = studentDetailPersistencePort.findStudentDetailByMemberEmail(member.getEmail());
        Score score = scorePersistencePort.findScoreByCategoryNameAndStudentDetailStudentCodeWithLock(categoryName, studentDetail.getStudentCode());

        score.plusValue(1);

        Evidence evidence = createEvidence(score, activityType);
        ActivityEvidence activityEvidence = createActivityEvidence(evidence, title, content, file);

        scorePersistencePort.saveScore(score);
        activityEvidencePersistencePort.saveActivityEvidence(activityEvidence);
        applicationEventPublisher.publishEvent(new ScoreUpdatedEvent(studentDetail.getStudentCode()));
    }

    private Evidence createEvidence(Score score, EvidenceType activityType) {
        return Evidence.builder()
                .score(score)
                .reviewStatus(ReviewStatus.PENDING)
                .evidenceType(activityType)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private ActivityEvidence createActivityEvidence(Evidence evidence, String title, String content, MultipartFile file) {
        String imageUrl = file != null && !file.isEmpty() ? uploadFile(file) : null;
        return ActivityEvidence.builder()
                .id(evidence)
                .title(title)
                .content(content)
                .imageUrl(imageUrl)
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