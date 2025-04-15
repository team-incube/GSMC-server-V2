package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
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
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.global.security.jwt.service.CurrentMemberProvider;
import team.incude.gsmc.v2.global.thirdparty.aws.exception.S3UploadFailedException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateActivityEvidenceService implements CreateActivityEvidenceUseCase {

    private final ActivityEvidencePersistencePort activityEvidencePersistencePort;
    private final ScorePersistencePort scorePersistencePort;
    private final S3Port s3Port;
    private final CurrentMemberProvider currentMemberProvider;

    @Override
    @Transactional
    public void execute(String categoryName, String title, String content, MultipartFile file, EvidenceType activityType) {
        Member member = currentMemberProvider.getCurrentUser();
        Score score = scorePersistencePort.findScoreByCategoryNameAndMemberEmail(categoryName, member.getEmail());

        Evidence newEvidence = createEvidence(score, activityType);
        ActivityEvidence newActivityEvidence = createActivityEvidence(newEvidence, title, content, file);
        saveActivityEvidence(newActivityEvidence);
    }

    private ActivityEvidence createActivityEvidence(Evidence evidence, String title, String content, MultipartFile file) {
        return ActivityEvidence.builder()
                .id(evidence)
                .title(title)
                .content(content)
                .imageUrl(uploadFile(file))
                .build();
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

    private String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;

        try {
            return s3Port.uploadFile(
                    UUID.randomUUID().toString(),
                    file.getInputStream()
            ).join();
        } catch (IOException e) {
            throw new S3UploadFailedException();
        }
    }

    private void saveActivityEvidence(ActivityEvidence activityEvidence) {
        activityEvidencePersistencePort.saveActivityEvidence(activityEvidence);
    }
}
