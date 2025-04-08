package team.incude.gsmc.v2.domain.certificate.application.usecase.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.certificate.application.port.CertificatePersistencePort;
import team.incude.gsmc.v2.domain.certificate.application.usecase.CreateCertificateUseCase;
import team.incude.gsmc.v2.domain.certificate.domain.Certificate;
import team.incude.gsmc.v2.domain.evidence.application.port.EvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.score.application.port.CategoryPersistencePort;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Category;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.domain.score.exception.ScoreLimitExceededException;
import team.incude.gsmc.v2.global.thirdparty.aws.exception.S3UploadFailedException;
import team.incude.gsmc.v2.global.util.ValueLimiterUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class CreateCertificateService implements CreateCertificateUseCase {



    @Override
    public void execute(String name, LocalDate acquisitionDate, MultipartFile file) {

    }
}

//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class CreateCertificateService implements CreateCertificateUseCase {
//
//    private final CertificatePersistencePort certificatePersistencePort;
//    private final EvidencePersistencePort evidencePersistencePort;
//    private final OtherEvidencePersistencePort otherEvidencePersistencePort;
//    private final MemberPersistencePort memberPersistencePort;
//    private final CategoryPersistencePort categoryPersistencePort;
//    private final ScorePersistencePort scorePersistencePort;
//    private final S3Port s3Port;
//
//    private final EntityManager entityManager;
//
//    private static final String CATEGORY_NAME = "MAJOR-CERTIFICATE-NUM";
//
//    // TODO: 인증/인가 구현 전 Mock 메서드
//    private void setSecurityContext() {
//        Authentication Authentication =
//                new UsernamePasswordAuthenticationToken("s24058@gsm.hs.kr","");
//        SecurityContextHolder.getContext().setAuthentication(Authentication);
//    }
//
//    // TODO: private 메서드 삭제 시 트랜젝션을 컨벤션에 따라 클래스에 적용
//    @Override
//    @Transactional
//    public void execute(String name, LocalDate acquisitionDate, MultipartFile file) {
//        setSecurityContext();
//
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        Score score = scorePersistencePort.findScoreByNameAndEmail(name, email);
//        Category category = categoryPersistencePort.findCategoryByName(CATEGORY_NAME);
//
//        Score newScore;
//
//        if (score != null) {
//            // Existing score logic
//            if (ValueLimiterUtil.isExceedingLimit(score.getValue() + 1, score.getCategory().getMaximumValue())) {
//                throw new ScoreLimitExceededException();
//            }
//
//            newScore = Score.builder()
//                    .id(score.getId())
//                    .category(category)
//                    .member(score.getMember())
//                    .value(score.getValue() + 1)
//                    .build();
//        } else {
//            // New score logic
//            newScore = Score.builder()
//                    .category(category)
//                    .member(memberPersistencePort.findMemberByEmail(email))
//                    .value(1)
//                    .build();
//        }
//
//        // Now that the score is persisted, create the evidence
//        Evidence evidence = Evidence.builder()
//                .score(newScore) // Now score is saved, valid reference
//                .evidenceType(EvidenceType.CERTIFICATE)
//                .reviewStatus(ReviewStatus.PENDING)
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .build();
//
//        scorePersistencePort.saveScore(evidence.getScore());
//        evidencePersistencePort.saveEvidence(evidence); // Save the evidence
//
//        try {
//            CompletableFuture<String> fileUriFuture = s3Port.uploadFile(file.getName(), file.getInputStream());
//            String fileUri = fileUriFuture.get();
//            otherEvidencePersistencePort.saveOtherEvidence(
//                    OtherEvidence.builder()
//                            .id(evidence) // Link evidence to other evidence
//                            .fileUri(fileUri)
//                            .build()
//            );
//        } catch (Exception e) {
//            throw new S3UploadFailedException();
//        }
//
//        certificatePersistencePort.saveCertificate(
//                Certificate.builder()
//                        .member(memberPersistencePort.findMemberByEmail(email))
//                        .name(name)
//                        .evidence(OtherEvidence.builder().id(evidence).fileUri("").build()) // Correct reference
//                        .acquisitionDate(acquisitionDate)
//                        .build()
//        );
//    }
//}