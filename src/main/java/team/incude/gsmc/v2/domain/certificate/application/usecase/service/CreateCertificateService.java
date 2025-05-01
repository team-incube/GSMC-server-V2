package team.incude.gsmc.v2.domain.certificate.application.usecase.service;

import lombok.RequiredArgsConstructor;
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

/**
 * 자격증 생성 유스케이스를 구현한 서비스 클래스입니다.
 * <p>{@link CreateCertificateUseCase}를 구현하며, 자격증 생성과 함께 관련 점수, 증빙 파일, 증거 객체를 생성하고 저장합니다.
 * <p>주요 처리 과정:
 * <ul>
 *   <li>카테고리 점수 확인 및 증가</li>
 *   <li>S3에 증빙 파일 업로드</li>
 *   <li>Evidence 및 OtherEvidence 생성</li>
 *   <li>자격증 저장</li>
 * </ul>
 *
 * <p>자격증 이름에 따라 자동으로 카테고리가 매핑되며, 점수 제한 초과 시 예외가 발생합니다.
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

    private static final String MAJOR_CERTIFICATE_CATEGORY_NAME = "MAJOR-CERTIFICATE_NUM";
    private static final String HUMANITIES_CERTIFICATE_KOREAN_HISTORY_CATEGORY_NAME = "HUMANITIES-CERTIFICATE-KOREAN_HISTORY";
    private static final String HUMANITIES_CERTIFICATE_CHINESE_CHARACTER_CATEGORY_NAME = "HUMANITIES-CERTIFICATE-CHINESE_CHARACTER";
    private final CurrentMemberProvider currentMemberProvider;

    /**
     * 자격증을 생성합니다.
     * <p>자격증 이름, 취득일, 증빙 파일을 받아 해당 정보를 기반으로 자격증을 생성하고 관련된 점수 및 증빙자료를 저장합니다.
     * @param name 자격증 이름
     * @param acquisitionDate 자격증 취득일
     * @param file 자격증 증빙 파일
     */
    @Override
    public void execute(String name, LocalDate acquisitionDate, MultipartFile file) {
        Member member = currentMemberProvider.getCurrentUser();

        Score updatedScore = updateScore(member, name);
        Score savedScore = scorePersistencePort.saveScore(updatedScore);

        Evidence evidence = createEvidence(savedScore);
        String fileUri = uploadFileToS3(file);
        OtherEvidence otherEvidence = otherEvidencePersistencePort.saveOtherEvidence(createOtherEvidence(evidence, fileUri));

        saveCertificate(name, member, acquisitionDate, otherEvidence);
    }

    /**
     * 자격증 이름에 따라 카테고리를 매핑하고 점수를 증가시킵니다.
     * <p>점수가 존재하지 않으면 새로 생성하며, 최대 점수를 초과하는 경우 예외를 발생시킵니다.
     * @param member 자격증을 등록하는 사용자
     * @param certificateName 자격증 이름
     * @return 업데이트된 점수 객체
     * @throws ScoreLimitExceededException 최대 점수를 초과하는 경우
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
     * 자격증 이름에 따라 카테고리 이름을 결정합니다.
     * @param certificateName 자격증 이름
     * @return 카테고리 이름
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
     * 새 점수 객체를 생성합니다.
     * @param category 카테고리
     * @param member 사용자
     * @return 생성된 점수 객체
     */
    private Score createNewScore(Category category, Member member) {
        return Score.builder()
                .category(category)
                .member(member)
                .value(1)
                .build();
    }

    /**
     * 점수를 기반으로 Evidence 객체를 생성합니다.
     * @param score 점수
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
     * 증빙 파일을 S3에 업로드하고 URI를 반환합니다.
     * @param file 업로드할 파일
     * @return 업로드된 파일의 URI
     * @throws S3UploadFailedException 업로드 실패 시
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
     * @param evidence 증거 객체
     * @param fileUri 파일 URI
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
     * @param name 자격증 이름
     * @param member 사용자
     * @param acquisitionDate 자격증 취득일
     * @param otherEvidence 연관 증빙 정보
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
}