package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.application.port.EvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.usecase.CreateOtherScoringEvidenceUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.score.application.port.CategoryPersistencePort;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Category;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.domain.score.exception.CategoryNotFoundException;
import team.incude.gsmc.v2.domain.score.exception.ScoreLimitExceededException;
import team.incude.gsmc.v2.global.event.FileUploadEvent;
import team.incude.gsmc.v2.global.event.ScoreUpdatedEvent;
import team.incude.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;
import team.incude.gsmc.v2.global.thirdparty.aws.exception.S3UploadFailedException;
import team.incude.gsmc.v2.global.thirdparty.discord.service.DiscordAlertService;
import team.incude.gsmc.v2.global.util.ValueLimiterUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;


/**
 * 점수 기반 기타 증빙자료 생성을 처리하는 유스케이스 구현 클래스입니다.
 * <p>{@link CreateOtherScoringEvidenceUseCase}를 구현하며, 카테고리 이름, 파일, 점수 값을 기반으로
 * 점수 기반 증빙자료를 생성하고 저장합니다.
 * <p>기존 점수가 없으면 새로 생성하고, 존재하면 값을 갱신하며,
 * 점수 상한 초과 시 {@link ScoreLimitExceededException}을 발생시킵니다.
 * 또한 점수 갱신 이후 {@link ScoreUpdatedEvent}를 발행합니다.
 * @author suuuuuuminnnnnn
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CreateOtherScoringEvidenceService implements CreateOtherScoringEvidenceUseCase {

    private final EvidencePersistencePort evidencePersistencePort;
    private final ScorePersistencePort scorePersistencePort;
    private final CurrentMemberProvider currentMemberProvider;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final OtherEvidencePersistencePort otherEvidencePersistencePort;
    private final CategoryPersistencePort categoryPersistencePort;
    private final DiscordAlertService discordAlertService;

    /**
     * 점수 기반 기타 증빙자료를 생성하거나 점수를 갱신합니다.
     * <p>카테고리와 사용자 정보를 기반으로 점수를 조회 또는 생성한 후,
     * S3에 파일을 업로드하고 증빙자료를 저장하며 점수 갱신 이벤트를 발행합니다.
     * @param categoryName 증빙자료 카테고리 이름
     * @param file 첨부할 파일
     * @param value 점수 값
     * @throws ScoreLimitExceededException 점수가 카테고리 최대값을 초과할 경우
     * @throws S3UploadFailedException 파일 업로드 실패 시
     */
    @Override
    public void execute(String categoryName, MultipartFile file, int value) {
        Member member = currentMemberProvider.getCurrentUser();
        Score score = scorePersistencePort.findScoreByCategoryNameAndMemberEmailWithLock(categoryName, member.getEmail());
        Category category = categoryPersistencePort.findAllCategory()
                .stream()
                .filter(cat -> cat.getName().equals(categoryName))
                .findFirst()
                .orElseThrow(CategoryNotFoundException::new);
        checkValueByMaximumValue(category, value);

        if (score == null) {
            score = createScore(value, category, member);
        } else {
            score = createScore(score, value);
        }

        score = scorePersistencePort.saveScore(score);

        EvidenceType evidenceType = categoryMap.get(categoryName);
        Evidence evidence = createEvidence(score, evidenceType);
        evidence = evidencePersistencePort.saveEvidence(evidence);
        OtherEvidence otherEvidence = createOtherEvidence(evidence, file);

        otherEvidencePersistencePort.saveOtherEvidence(evidence, otherEvidence);
        applicationEventPublisher.publishEvent(new ScoreUpdatedEvent(member.getEmail()));

        try {
            applicationEventPublisher.publishEvent(new FileUploadEvent(
                    evidence.getId(),
                    file.getOriginalFilename(),
                    file.getInputStream(),
                    evidence.getEvidenceType()
            ));
        } catch (IOException e) {
            discordAlertService.sendEvidenceUploadFailureAlert(
                    evidence.getId(),
                    file.getOriginalFilename(),
                    member.getEmail(),
                    e
            );
            throw new S3UploadFailedException();
        }
    }

    /**
     * 지정된 점수 값이 카테고리의 최대값을 초과하는지 확인합니다.
     * @param category 점수 제한을 참조할 카테고리
     * @param value 입력된 점수 값
     * @throws ScoreLimitExceededException 초과 시 예외 발생
     */
    private void checkValueByMaximumValue(Category category, int value) {
        if (ValueLimiterUtil.isExceedingLimit(value, category.getMaximumValue())) {
            throw new ScoreLimitExceededException();
        }
    }

    /**
     * 새 점수 객체를 생성합니다.
     * @param value 점수 값
     * @param category 카테고리
     * @param member 사용자
     * @return 생성된 Score 객체
     */
    private Score createScore(int value, Category category, Member member) {
        return Score.builder()
                .value(value)
                .category(category)
                .member(member)
                .build();
    }

    /**
     * 기존 점수를 기반으로 값을 갱신한 새 Score 객체를 생성합니다.
     * @param score 기존 점수 객체
     * @param value 갱신할 점수 값
     * @return 새 Score 객체
     */
    private Score createScore(Score score, int value) {
        return Score.builder()
                .id(score.getId())
                .value(value)
                .category(score.getCategory())
                .member(score.getMember())
                .build();
    }

    /**
     * 점수 및 증빙자료 타입을 기반으로 Evidence 객체를 생성합니다.
     * @param score 연관 점수
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
     * 파일을 업로드한 후, Evidence와 연관된 OtherEvidence 객체를 생성합니다.
     * @param evidence 연관 Evidence 객체
     * @param file 첨부 파일
     * @return 생성된 OtherEvidence 객체
     */
    private OtherEvidence createOtherEvidence(Evidence evidence, MultipartFile file) {
        String tempUploadKey = "upload_" + file.getOriginalFilename();

        return OtherEvidence.builder()
                .id(evidence)
                .fileUri(tempUploadKey)
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