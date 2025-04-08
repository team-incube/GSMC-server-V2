package team.incude.gsmc.v2.domain.certificate.application.usecase.service;

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
import java.util.concurrent.CompletableFuture;

@Slf4j
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
        String email = getAuthenticatedEmail();
        Member member = memberPersistencePort.findMemberByEmail(email);

        // 1. 자격증 점수 업데이트
        Score updatedScore = updateScore(name, member);

        // 2. 증빙 자료 생성 및 저장
        Evidence evidence = createEvidence(updatedScore);

        // 3. 파일 업로드 후 다른 증빙 자료로 연결
        String fileUri = uploadFileToS3(file);
        OtherEvidence otherEvidence = createOtherEvidence(evidence, fileUri);

        // 4. 자격증 등록
        saveCertificate(name, member, acquisitionDate, otherEvidence);
    }

    private String getAuthenticatedEmail() {
        // 현재 인증된 사용자의 이메일을 가져오는 부분 (예시로 mock 이메일 사용)
        setSecurityContext("s24058@gsm.hs.kr");
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private Score updateScore(String name, Member member) {
        // 기존 점수를 가져옴
        Score score = scorePersistencePort.findScoreByNameAndEmail(name, member.getEmail());
        Category category = categoryPersistencePort.findCategoryByName(CATEGORY_NAME);

        if (score != null) {
            // 점수를 1점 올리기 전에 제한을 확인
            if (ValueLimiterUtil.isExceedingLimit(score.getValue() + 1, category.getMaximumValue())) {
                throw new ScoreLimitExceededException();
            }

            // 점수 업데이트: 새 Score 객체를 빌더 패턴으로 생성
            score = Score.builder()
                    .id(score.getId())
                    .category(category)
                    .member(score.getMember())
                    .value(score.getValue() + 1)
                    .semester(score.getSemester())
                    .build();
        } else {
            score = createNewScore(category, member);
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
            CompletableFuture<String> fileUriFuture = s3Port.uploadFile(file.getOriginalFilename(), file.getInputStream());
            return fileUriFuture.get();
        } catch (Exception e) {
            throw new S3UploadFailedException();
        }
    }

    private OtherEvidence createOtherEvidence(Evidence evidence, String fileUri) {
        // Evidence와 파일 URI를 사용해 OtherEvidence를 생성
        return OtherEvidence.builder()
                .id(evidence)
                .fileUri(fileUri)
                .build();
    }

    private void saveCertificate(String name, Member member, LocalDate acquisitionDate, OtherEvidence otherEvidence) {
        log.info("Member info"+" : {}", member.getEmail());
        Certificate certificate = Certificate.builder()
                .member(member)
                .name(name)
                .evidence(otherEvidence)
                .acquisitionDate(acquisitionDate)
                .build();
        certificatePersistencePort.saveCertificate(certificate);
    }

    // 인증/인가를 위한 보조 메서드 (현재는 mock 이메일을 사용)
    private void setSecurityContext(String email) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, "");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}