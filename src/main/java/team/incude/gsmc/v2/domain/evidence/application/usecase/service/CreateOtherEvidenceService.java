package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
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
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.global.security.jwt.usecase.service.CurrentMemberProvider;
import team.incude.gsmc.v2.global.thirdparty.aws.exception.S3UploadFailedException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateOtherEvidenceService implements CreateOtherEvidenceUseCase {

    private final OtherEvidencePersistencePort otherEvidencePersistencePort;
    private final S3Port s3Port;
    private final ScorePersistencePort scorePersistencePort;
    private final CurrentMemberProvider currentMemberProvider;

    @Override
    @Transactional
    public void execute(String categoryName, MultipartFile file) {
        Member member = currentMemberProvider.getCurrentUser();
        Score score = scorePersistencePort.findScoreByCategoryNameAndMemberEmail(categoryName, member.getEmail());

        Evidence newEvidence = createEvidence(score);
        OtherEvidence newOtherEvidence = createOtherEvidence(newEvidence, file);
        saveOtherEvidence(newOtherEvidence);
    }

    private Evidence createEvidence(Score score) {
        return Evidence.builder()
                .score(score)
                .reviewStatus(ReviewStatus.PENDING)
                // TODO: 나중에 카테고리 확정되면 변경해야함
                .evidenceType(EvidenceType.FOREIGN_LANGUAGE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private OtherEvidence createOtherEvidence(Evidence evidence, MultipartFile file) {
        return OtherEvidence.builder()
                .id(evidence)
                .fileUri(uploadFile(file))
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

    private void saveOtherEvidence(OtherEvidence otherEvidence) {
        otherEvidencePersistencePort.saveOtherEvidence(otherEvidence);
    }
}
