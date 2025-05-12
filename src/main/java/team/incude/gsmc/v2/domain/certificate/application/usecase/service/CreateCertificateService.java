package team.incude.gsmc.v2.domain.certificate.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.certificate.application.port.CertificatePersistencePort;
import team.incude.gsmc.v2.domain.certificate.application.usecase.CreateCertificateUseCase;
import team.incude.gsmc.v2.domain.certificate.domain.Certificate;
import team.incude.gsmc.v2.domain.certificate.exception.DuplicateCertificateException;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.score.application.port.CategoryPersistencePort;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Category;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.domain.score.exception.ScoreLimitExceededException;
import team.incude.gsmc.v2.global.security.jwt.usecase.service.CurrentMemberProvider;
import team.incude.gsmc.v2.global.thirdparty.aws.exception.S3UploadFailedException;
import team.incude.gsmc.v2.global.util.ValueLimiterUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 자격증 생성을 담당하는 유스케이스 구현체입니다.
 * <p>
 * 자격증 이름과 취득일, 파일 정보를 바탕으로 S3 업로드 및 점수 업데이트, 증빙 생성, 자격증 저장 등의 기능을 수행합니다.
 * </p>
 * @author snowykte0426
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CreateCertificateService implements CreateCertificateUseCase {

    private final CertificatePersistencePort certificatePersistencePort;
    private final OtherEvidencePersistencePort otherEvidencePersistencePort;
    private final CategoryPersistencePort categoryPersistencePort;
    private final ScorePersistencePort scorePersistencePort;
    private final S3Port s3Port;
    private final CurrentMemberProvider currentMemberProvider;

    private static final String MAJOR_CERTIFICATE_CATEGORY_NAME = "MAJOR-CERTIFICATE_NUM";
    private static final String HUMANITIES_CERTIFICATE_KOREAN_HISTORY_CATEGORY_NAME = "HUMANITIES-CERTIFICATE-KOREAN_HISTORY";
    private static final String HUMANITIES_CERTIFICATE_CHINESE_CHARACTER_CATEGORY_NAME = "HUMANITIES-CERTIFICATE-CHINESE_CHARACTER";

    /**
     * 자격증 생성을 수행합니다.
     * @param name            자격증 이름
     * @param acquisitionDate 자격증 취득일
     * @param file            자격증 증빙 파일
     */
    @Override
    public void execute(String name, LocalDate acquisitionDate, MultipartFile file) {
        Member member = currentMemberProvider.getCurrentUser();
        validateDuplicateCertificateType(member, name);
        Score updatedScore = updateScore(member, name);
        Score savedScore = scorePersistencePort.saveScore(updatedScore);
        Evidence evidence = createEvidence(savedScore);
        String fileUri = uploadFileToS3(file);
        OtherEvidence otherEvidence = otherEvidencePersistencePort.saveOtherEvidence(createOtherEvidence(evidence, fileUri));
        saveCertificate(name, member, acquisitionDate, otherEvidence);
    }

    /**
     * 자격증 이름에 따라 점수를 업데이트합니다.
     * 기존 점수가 존재하지 않으면 새로 생성합니다.
     * 점수 제한을 초과하면 예외를 발생시킵니다.
     * @param member          점수를 업데이트할 회원
     * @param certificateName 자격증 이름
     * @return 업데이트된 Score 객체
     */
    private Score updateScore(Member member, String certificateName) {
        String categoryName = resolveCategoryFromCertificateName(certificateName);
        Score score = scorePersistencePort.findScoreByCategoryNameAndMemberEmailWithLock(categoryName, member.getEmail());

        if (score == null) {
            score = createNewScore(categoryPersistencePort.findCategoryByName(categoryName), member);
        } else {
            if (ValueLimiterUtil.isExceedingLimit(score.getValue() + 1, score.getCategory().getMaximumValue())) {
                throw new ScoreLimitExceededException();
            }
            score.plusValue(1);
        }
        return score;
    }

    /**
     * 자격증 이름으로부터 점수 카테고리를 판별합니다.
     * @param certificateName 자격증 이름
     * @return 해당 이름에 대응하는 카테고리 이름
     */
    private String resolveCategoryFromCertificateName(String certificateName) {
        if (certificateName.startsWith("한국사 능력검정")) {
            return HUMANITIES_CERTIFICATE_KOREAN_HISTORY_CATEGORY_NAME;
        }
        if (certificateName.startsWith("한자검정시험")) {
            return HUMANITIES_CERTIFICATE_CHINESE_CHARACTER_CATEGORY_NAME;
        }
        return MAJOR_CERTIFICATE_CATEGORY_NAME;
    }

    /**
     * 새로운 점수 엔티티를 생성합니다.
     * @param category 점수 카테고리
     * @param member   대상 회원
     * @return 생성된 Score 객체
     */
    private Score createNewScore(Category category, Member member) {
        return Score.builder()
                .category(category)
                .member(member)
                .value(1)
                .build();
    }

    /**
     * 점수에 대한 증빙자료 엔티티를 생성합니다.
     * @param score 점수 엔티티
     * @return 생성된 Evidence 객체
     */
    private Evidence createEvidence(Score score) {
        return Evidence.builder()
                .score(score)
                .evidenceType(EvidenceType.CERTIFICATE)
                .reviewStatus(ReviewStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * 파일을 S3에 업로드하고 업로드된 파일의 URI를 반환합니다.
     * @param file 업로드할 파일
     * @return 업로드된 파일의 URI
     * @throws S3UploadFailedException 파일 업로드에 실패한 경우
     */
    private String uploadFileToS3(MultipartFile file) {
        try {
            return s3Port.uploadFile(file.getOriginalFilename(), file.getInputStream()).join();
        } catch (IOException e) {
            throw new S3UploadFailedException();
        }
    }

    /**
     * Evidence와 파일 URI를 기반으로 OtherEvidence 객체를 생성합니다.
     * @param evidence 증빙 객체
     * @param fileUri  S3에 업로드된 파일 URI
     * @return 생성된 OtherEvidence 객체
     */
    private OtherEvidence createOtherEvidence(Evidence evidence, String fileUri) {
        return OtherEvidence.builder()
                .id(evidence)
                .fileUri(fileUri)
                .build();
    }

    /**
     * 자격증 정보를 저장합니다.
     * @param name            자격증 이름
     * @param member          회원
     * @param acquisitionDate 자격증 취득일
     * @param otherEvidence   증빙자료 정보
     */
    private void saveCertificate(String name, Member member, LocalDate acquisitionDate, OtherEvidence otherEvidence) {
        Certificate certificate = Certificate.builder()
                .member(member)
                .name(name)
                .evidence(otherEvidence)
                .acquisitionDate(acquisitionDate)
                .build();
        certificatePersistencePort.saveCertificate(certificate);
    }

    /**
     * 동일 계열 자격증의 중복 제출 여부를 확인합니다.
     * 예: "한자검정시험(1)"이 존재하면 "한자검정시험(2)" 제출 차단
     * @param member          회원
     * @param certificateName 자격증 이름
     * @throws DuplicateCertificateException 중복인 경우 예외 발생
     */
    private void validateDuplicateCertificateType(Member member, String certificateName) {
        String typePrefix = resolveDuplicateRestrictedPrefix(certificateName);
        if (typePrefix == null) return;
        List<Certificate> lockedCertificates = certificatePersistencePort.findCertificateByMemberIdWithLock(member.getId());
        boolean exists = lockedCertificates.stream()
                .map(Certificate::getName)
                .anyMatch(name -> name.startsWith(typePrefix));
        if (exists) {
            throw new DuplicateCertificateException();
        }
    }

    /**
     * 중복 검사를 위해 비교할 자격증 이름 접두사를 반환합니다.
     * @param certificateName 자격증 이름
     * @return 접두사 문자열 (중복 검사 대상이 아니면 null)
     */
    private String resolveDuplicateRestrictedPrefix(String certificateName) {
        if (certificateName.startsWith("한국사 능력검정")) return "한국사 능력검정";
        if (certificateName.startsWith("한자검정시험")) return "한자검정시험";
        return null;
    }
}