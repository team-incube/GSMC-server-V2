package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
import team.incude.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.member.domain.StudentDetail;
import team.incude.gsmc.v2.domain.score.application.port.CategoryPersistencePort;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Category;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.domain.score.exception.ScoreLimitExceededException;
import team.incude.gsmc.v2.global.event.ScoreUpdatedEvent;
import team.incude.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;
import team.incude.gsmc.v2.global.thirdparty.aws.exception.S3UploadFailedException;
import team.incude.gsmc.v2.global.util.ValueLimiterUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 기타 증빙자료 생성을 담당하는 유스케이스 구현 클래스입니다.
 * <p>{@link CreateOtherEvidenceUseCase}를 구현하며, 카테고리 이름과 첨부 파일을 기반으로
 * 새로운 점수 기반 기타 증빙자료를 생성하고 저장합니다.
 * <p>점수 누적, S3 업로드, Evidence 및 OtherEvidence 엔티티 생성, 이벤트 발행 등을 수행합니다.
 * <p>점수 상한 초과 시 {@link ScoreLimitExceededException}을 발생시키며, 업로드 실패 시 {@link S3UploadFailedException}이 발생합니다.
 * @author suuuuuuminnnnnn
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CreateOtherEvidenceService implements CreateOtherEvidenceUseCase {

    private final S3Port s3Port;
    private final StudentDetailPersistencePort studentDetailPersistencePort;
    private final OtherEvidencePersistencePort otherEvidencePersistencePort;
    private final ScorePersistencePort scorePersistencePort;
    private final CurrentMemberProvider currentMemberProvider;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final CategoryPersistencePort categoryPersistencePort;

    /**
     * 기타 증빙자료를 생성하고 점수를 갱신하며 이벤트를 발행합니다.
     * <p>해당 증빙자료는 카테고리 이름에 매핑된 {@link EvidenceType}으로 저장되며,
     * 점수가 없으면 새로 생성하고, 존재하면 +1을 누적합니다.
     * <p>파일은 S3에 업로드되며, 업로드 실패 시 {@link S3UploadFailedException}이 발생합니다.
     * @param categoryName 점수를 누적할 카테고리 이름
     * @param file 증빙자료 파일
     * @throws ScoreLimitExceededException 점수 상한을 초과한 경우
     */
    @Override
    public void execute(String categoryName, MultipartFile file) {
        Member member = currentMemberProvider.getCurrentUser();
        StudentDetail studentDetail = studentDetailPersistencePort.findStudentDetailByMemberEmail(member.getEmail());
        Score score = scorePersistencePort.findScoreByCategoryNameAndStudentDetailStudentCodeWithLock(categoryName, studentDetail.getStudentCode());

        if (score == null) {
            score = createScore(categoryName, member);
        } else if (ValueLimiterUtil.isExceedingLimit(score.getValue() + 1, score.getCategory().getMaximumValue())) {
            throw new ScoreLimitExceededException();
        }
        score.plusValue(1);
        score = scorePersistencePort.saveScore(score);

        EvidenceType evidenceType = categoryMap.get(categoryName);
        Evidence evidence = createEvidence(score, evidenceType);
        OtherEvidence otherEvidence = createOtherEvidence(evidence, file);

        otherEvidencePersistencePort.saveOtherEvidence(evidence, otherEvidence);
        applicationEventPublisher.publishEvent(new ScoreUpdatedEvent(studentDetail.getStudentCode()));
    }

    /**
     * Evidence 도메인 객체를 생성합니다.
     * @param score 연결할 점수 객체
     * @param evidenceType 증빙자료 타입
     * @return 생성된 Evidence 객체
     */
    private Evidence createEvidence(Score score, EvidenceType evidenceType) {
        return Evidence.builder()
                .score(score)
                .reviewStatus(ReviewStatus.PENDING)
                .evidenceType(evidenceType)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * OtherEvidence 도메인 객체를 생성합니다.
     * <p>파일이 존재하면 업로드 후 해당 URI를 저장합니다.
     * @param evidence 연결할 Evidence 객체
     * @param file 첨부 파일 (선택)
     * @return 생성된 OtherEvidence 객체
     */
    private OtherEvidence createOtherEvidence(Evidence evidence, MultipartFile file) {
        String imageUrl = file != null && !file.isEmpty() ? uploadFile(file) : null;
        return OtherEvidence.builder()
                .id(evidence)
                .fileUri(imageUrl)
                .build();
    }

    /**
     * MultipartFile을 S3에 업로드하고 URL을 반환합니다.
     * @param file 업로드할 파일
     * @return 업로드된 파일의 URL
     * @throws S3UploadFailedException 업로드 실패 시
     */
    private String uploadFile(MultipartFile file) {
        try {
            return s3Port.uploadFile(
                    file.getOriginalFilename(),
                    file.getInputStream()
            ).join();
        } catch (IOException e) {
            throw new S3UploadFailedException();
        }
    }

    /**
     * 점수 테이블에 신규 Score 객체를 생성합니다.
     * @param categoryName 카테고리 이름
     * @param member 사용자
     * @return 생성된 Score 객체
     */
    private Score createScore(String categoryName, Member member) {
        Category category = categoryPersistencePort.findCategoryByName(categoryName);
        return Score.builder()
                .category(category)
                .value(0)
                .member(member)
                .build();
    }

    private static final Map<String, EvidenceType> categoryMap = Map.ofEntries(
            Map.entry("HUMANITIES-READING-READ_A_THON-TURTLE", EvidenceType.READ_A_THON),
            Map.entry("HUMANITIES-READING-READ_A_THON-CROCODILE", EvidenceType.READ_A_THON),
            Map.entry("HUMANITIES-READING-READ_A_THON-RABBIT_OVER", EvidenceType.READ_A_THON)
    );
}
