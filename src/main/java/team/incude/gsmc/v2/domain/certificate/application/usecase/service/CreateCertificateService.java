package team.incude.gsmc.v2.domain.certificate.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.certificate.application.port.CertificatePersistencePort;
import team.incude.gsmc.v2.domain.certificate.application.usecase.CreateCertificateUseCase;
import team.incude.gsmc.v2.domain.certificate.domain.Certificate;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.score.application.port.CategoryPersistencePort;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Category;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.domain.score.exception.ScoreLimitExceededException;
import team.incude.gsmc.v2.global.thirdparty.aws.exception.S3UploadFailedException;
import team.incude.gsmc.v2.global.util.ValueLimiterUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateCertificateService implements CreateCertificateUseCase {

    private final CertificatePersistencePort certificatePersistencePort;
    private final OtherEvidencePersistencePort otherEvidencePersistencePort;
    private final MemberPersistencePort memberPersistencePort;
    private final CategoryPersistencePort categoryPersistencePort;
    private final ScorePersistencePort scorePersistencePort;
    private final S3Port s3Port;

    private static final String CATEGORY_NAME = "MAJOR-CERTIFICATE-NUM";

    @Override
    public void execute(String name, LocalDate acquisitionDate, MultipartFile file) {
        String email = getAuthenticatedEmail();
        Member member = memberPersistencePort.findMemberByEmail(email);

        Score updatedScore = scorePersistencePort.saveScore(updateScore(member));

        Evidence evidence = createEvidence(updatedScore);

        String fileUri = uploadFileToS3(file);
        OtherEvidence otherEvidence = otherEvidencePersistencePort.saveOtherEvidence(createOtherEvidence(evidence, fileUri));

        saveCertificate(name, member, acquisitionDate, otherEvidence);
    }

    // TODO: 인증인가 구현 시 변경 필
    private String getAuthenticatedEmail() {
        setSecurityContext("s24058@gsm.hs.kr");
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private Score updateScore(Member member) {
        Score score = scorePersistencePort.findScoreByCategoryNameAndMemberEmailWithLock(CATEGORY_NAME, member.getEmail());
        if (score == null) {
            score = createNewScore(categoryPersistencePort.findCategoryByName(CATEGORY_NAME), member);
        } else {
            if (ValueLimiterUtil.isExceedingLimit(score.getValue() + 1, score.getCategory().getMaximumValue())) {
                throw new ScoreLimitExceededException();
            }
            score.plusValue(1);
        }
        return score;
    }

    private Score createNewScore(Category category, Member member) {
        return Score.builder()
                .category(category)
                .member(member)
                .value(1)
                .build();
    }

    private Evidence createEvidence(Score score) {
        return Evidence.builder()
                .score(score)
                .evidenceType(EvidenceType.CERTIFICATE)
                .reviewStatus(ReviewStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private String uploadFileToS3(MultipartFile file) {
        try {
            return s3Port.uploadFile(file.getOriginalFilename(), file.getInputStream()).join();
        } catch (Exception e) {
            throw new S3UploadFailedException();
        }
    }

    private OtherEvidence createOtherEvidence(Evidence evidence, String fileUri) {
        return OtherEvidence.builder()
                .id(evidence)
                .fileUri(fileUri)
                .build();
    }

    private void saveCertificate(String name, Member member, LocalDate acquisitionDate, OtherEvidence otherEvidence) {
        Certificate certificate = Certificate.builder()
                .member(member)
                .name(name)
                .evidence(otherEvidence)
                .acquisitionDate(acquisitionDate)
                .build();
        certificatePersistencePort.saveCertificate(certificate);
    }

    // TODO: auth 구현 전 임시 코드
    private void setSecurityContext(String email) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, "");
        SecurityContextHolder.clearContext();
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}