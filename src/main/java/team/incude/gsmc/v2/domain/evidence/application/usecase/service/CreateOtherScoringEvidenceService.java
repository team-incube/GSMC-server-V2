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
        Score score = scorePersistencePort.findScoreByCategoryNameAndStudentDetailStudentCodeWithLock(categoryName, studentDetail.getStudentCode());

        Score newScore = createScore(score, value);

        EvidenceType evidenceType = categoryMap.get(categoryName);
        Evidence evidence = createEvidence(score, evidenceType);
        OtherEvidence otherEvidence = createOtherEvidence(evidence, file);

        scorePersistencePort.saveScore(newScore);
        otherEvidencePersistencePort.saveOtherEvidence(otherEvidence);
        applicationEventPublisher.publishEvent(new ScoreUpdatedEvent(studentDetail.getStudentCode()));
    }

    private Score createScore(Score score, int value) {
        return Score.builder()
                .id(score.getId())
                .value(value)
                .category(score.getCategory())
                .member(score.getMember())
                .build();
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

    private String uploadFile(MultipartFile file) {
        try {
            return s3Port.uploadFile(
                    file.getOriginalFilename(),
                    file.getInputStream()
            ).join();
        } catch (
                IOException e) {
            throw new S3UploadFailedException();
        }
    }

    private OtherEvidence createOtherEvidence(Evidence evidence, MultipartFile file) {
        String imageUrl = file != null && !file.isEmpty() ? uploadFile(file) : null;
        return OtherEvidence.builder()
                .id(evidence)
                .fileUri(imageUrl)
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
