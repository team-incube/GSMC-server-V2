package team.incude.gsmc.v2.domain.certificate.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.certificate.application.port.CertificatePersistencePort;
import team.incude.gsmc.v2.domain.certificate.application.usecase.CreateCertificateUseCase;
import team.incude.gsmc.v2.domain.evidence.application.port.EvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.member.exception.MemberNotFoundException;
import team.incude.gsmc.v2.domain.score.application.port.CategoryPersistencePort;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.domain.score.exception.CategoryNotFoundException;
import team.incude.gsmc.v2.domain.score.exception.ScoreLimitExceededException;
import team.incude.gsmc.v2.global.thirdparty.aws.exception.S3UploadFailedException;
import team.incude.gsmc.v2.global.util.ValueLimiterUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateCertificateService implements CreateCertificateUseCase {

    private final CertificatePersistencePort certificatePersistencePort;
    private final EvidencePersistencePort evidencePersistencePort;
    private final OtherEvidencePersistencePort otherEvidencePersistencePort;
    private final MemberPersistencePort memberPersistencePort;
    private final CategoryPersistencePort categoryPersistencePort;
    private final ScorePersistencePort scorePersistencePort;
    private final S3Port s3Port;

    private static final String CATEGORY_NAME = "MAJOR-CERTIFICATE-NUM";

    @Override
    public void execute(String name, LocalDate acquisitionDate, MultipartFile file) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Score score = scorePersistencePort.findScoreByNameAndEmail(name, email).orElse(null);

        if (score != null) {
            if (ValueLimiterUtil.isExceedingLimit(score.getValue() + 1, score.getCategory().getMaximumValue())) {
                throw new ScoreLimitExceededException();
            }
            score = Score.builder()
                    .category(score.getCategory())
                    .member(score.getMember())
                    .value(score.getValue() + 1)
                    .build();
        } else {
            score = Score.builder()
                    .category(categoryPersistencePort.findCategoryByName(CATEGORY_NAME).orElseThrow(CategoryNotFoundException::new))
                    .member(memberPersistencePort.findMemberByEmail(email).orElseThrow(MemberNotFoundException::new))
                    .value(1)
                    .build();
        }

        scorePersistencePort.saveScore(score);

        Evidence evidence = Evidence.builder()
                .score(score)
                .evidenceType(EvidenceType.CERTIFICATE)
                .reviewStatus(ReviewStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        evidencePersistencePort.saveEvidence(evidence);

        try {
            CompletableFuture<String> fileUriFuture = s3Port.uploadFile(file.getName(), file.getInputStream());
            String fileUri = fileUriFuture.get();
            otherEvidencePersistencePort.saveOtherEvidence(
                    OtherEvidence.builder()
                            .id(evidence)
                            .fileUri(fileUri)
                            .build()
            );
        } catch (Exception e) {
            throw new S3UploadFailedException();
        }
    }
}