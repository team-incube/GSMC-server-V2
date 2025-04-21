package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incude.gsmc.v2.domain.evidence.application.usecase.CreateOtherScoringEvidenceUseCase;
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
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateOtherScoringEvidenceService implements CreateOtherScoringEvidenceUseCase {

    private final S3Port s3Port;
    private final StudentDetailPersistencePort studentDetailPersistencePort;
    private final ScorePersistencePort scorePersistencePort;
    private final CurrentMemberProvider currentMemberProvider;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final OtherEvidencePersistencePort otherEvidencePersistencePort;

    @Override
    @Transactional
    public void execute(String categoryName, MultipartFile file, int value) {
        Member member = currentMemberProvider.getCurrentUser();
        StudentDetail studentDetail = studentDetailPersistencePort.findStudentDetailByMemberEmail(member.getEmail());
        Score score = scorePersistencePort.findScoreByCategoryNameAndMemberEmail(categoryName, member.getEmail());

        score.plusValue(value);

        EvidenceType evidenceType = findEvidenceType(categoryName);
        Evidence evidence = createEvidence(score, evidenceType);
        String fileUrl = uploadFile(file);
        OtherEvidence otherEvidence = createOtherEvidence(evidence, fileUrl);

        scorePersistencePort.saveScore(score);
        otherEvidencePersistencePort.saveOtherEvidence(otherEvidence);
        applicationEventPublisher.publishEvent(new ScoreUpdatedEvent(studentDetail.getStudentCode()));
    }

    private Evidence createEvidence(Score score, EvidenceType evidenceType) {
        return Evidence.builder()
                .score(score)
                .reviewStatus(ReviewStatus.PENDING)
                .evidenceType(evidenceType)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private EvidenceType findEvidenceType(String categoryName) {
        return categoryMap.get(categoryName);
    }

    private String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;

        try {
            return s3Port.uploadFile(
                    UUID.randomUUID().toString(),
                    file.getInputStream()
            ).join();
        } catch (
                IOException e) {
            throw new S3UploadFailedException();
        }
    }

    private OtherEvidence createOtherEvidence(Evidence evidence, String fileUrl) {
        return OtherEvidence.builder()
                .id(evidence)
                .fileUri(fileUrl)
                .build();
    }

    private static final Map<String, EvidenceType> categoryMap = Map.ofEntries(
            Map.entry("FOREIGN_LANG-TOEIC_SCORE", EvidenceType.TOEIC),
            Map.entry("FOREIGN_LANG-TOEFL_SCORE", EvidenceType.TOEFL),
            Map.entry("FOREIGN_LANG-TEPS_SCORE", EvidenceType.TEPS),
            Map.entry("FOREIGN_LANG-TOEIC_SPEAKING_LEVEL", EvidenceType.TOEIC_SPEAKING),
            Map.entry("FOREIGN_LANG-OPIC_SCORE", EvidenceType.OPIC),
            Map.entry("FOREIGN_LANG-JPT_SCORE", EvidenceType.JPT),
            Map.entry("FOREIGN_LANG-CPT_SCORE", EvidenceType.CPT),
            Map.entry("FOREIGN_LANG-HSK_SCORE", EvidenceType.HSK),
            Map.entry("MAJOR-TOPCIT_SCORE", EvidenceType.TOPCIT)
    );
}
