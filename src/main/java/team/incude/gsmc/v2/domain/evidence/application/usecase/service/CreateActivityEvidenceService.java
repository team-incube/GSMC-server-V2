package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incude.gsmc.v2.domain.evidence.application.usecase.CreateActivityEvidenceUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.member.domain.StudentDetail;
import team.incude.gsmc.v2.domain.score.application.port.CategoryPersistencePort;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Category;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.domain.score.exception.CategoryNotFoundException;
import team.incude.gsmc.v2.domain.score.exception.ScoreLimitExceededException;
import team.incude.gsmc.v2.global.event.DraftEvidenceDeleteEvent;
import team.incude.gsmc.v2.global.event.ScoreUpdatedEvent;
import team.incude.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;
import team.incude.gsmc.v2.global.thirdparty.aws.exception.S3UploadFailedException;
import team.incude.gsmc.v2.global.util.ValueLimiterUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 활동 증빙자료 생성을 처리하는 유스케이스 구현 클래스입니다.
 * <p>{@link CreateActivityEvidenceUseCase}를 구현하며, 카테고리 이름, 제목, 내용, 파일/이미지, 활동 유형을 바탕으로
 * 새로운 활동 증빙자료를 생성하고 저장합니다.
 * <p>점수 누적과 관련된 로직도 함께 수행되며, 카테고리 점수가 없으면 생성하고, 존재 시에는 점수를 증가시킵니다.
 * <p>해당 작업 이후 {@link ScoreUpdatedEvent} 및 {@link DraftEvidenceDeleteEvent} 이벤트를 발행합니다.
 * <p>파일이 존재할 경우 S3에 업로드되며, 실패 시 {@link S3UploadFailedException}을 발생시킵니다.
 * @author suuuuuuminnnnnn
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CreateActivityEvidenceService implements CreateActivityEvidenceUseCase {

    private final ActivityEvidencePersistencePort activityEvidencePersistencePort;
    private final CategoryPersistencePort categoryPersistencePort;
    private final ScorePersistencePort scorePersistencePort;
    private final StudentDetailPersistencePort studentDetailPersistencePort;
    private final S3Port s3Port;
    private final CurrentMemberProvider currentMemberProvider;
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 활동 증빙자료를 생성하고 점수를 갱신하며 관련 이벤트를 발행합니다.
     * <p>카테고리 이름과 증빙자료 정보(제목, 내용, 첨부파일/이미지)를 기반으로 활동 증빙자료를 생성하며,
     * 점수 테이블에서 해당 카테고리에 대해 +1을 누적합니다.
     * <p>임시저장 ID가 존재하는 경우, 임시저장 삭제 이벤트를 함께 발행합니다.
     * @param categoryName 카테고리 이름
     * @param title 활동 제목
     * @param content 활동 내용
     * @param file 첨부 파일 (선택)
     * @param imageUrl 외부 이미지 URL (선택)
     * @param activityType 활동 유형
     * @param draftId 임시저장 ID (선택)
     */
    @Override
    public void execute(String categoryName, String title, String content, MultipartFile file, String imageUrl, EvidenceType activityType, UUID draftId) {
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

        Evidence evidence = createEvidence(score, activityType);
        ActivityEvidence activityEvidence = createActivityEvidence(evidence, title, content, file, imageUrl);

        activityEvidencePersistencePort.saveActivityEvidence(evidence, activityEvidence);
        applicationEventPublisher.publishEvent(new ScoreUpdatedEvent(studentDetail.getStudentCode()));
        applicationEventPublisher.publishEvent(new DraftEvidenceDeleteEvent(draftId));
    }

    /**
     * Evidence 도메인 객체를 생성합니다.
     * @param score 해당 활동이 속한 점수 엔티티
     * @param activityType 활동 유형
     * @return 생성된 Evidence 객체
     */
    private Evidence createEvidence(Score score, EvidenceType activityType) {
        return Evidence.builder()
                .score(score)
                .reviewStatus(ReviewStatus.PENDING)
                .evidenceType(activityType)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * ActivityEvidence 도메인 객체를 생성합니다.
     * <p>파일이 존재하면 업로드 후 이미지 URL을 생성하며, 그렇지 않으면 imageUrlParam 값을 사용합니다.
     * @param evidence 연관된 Evidence 객체
     * @param title 활동 제목
     * @param content 활동 내용
     * @param file 첨부 파일 (선택)
     * @param imageUrlParam 외부 이미지 URL (선택)
     * @return 생성된 ActivityEvidence 객체
     */
    private ActivityEvidence createActivityEvidence(Evidence evidence, String title, String content, MultipartFile file, String imageUrlParam) {
        String imageUrl = null;

        if (file != null && !file.isEmpty()) {
            imageUrl = uploadFile(file);
        } else if (imageUrlParam != null && !imageUrlParam.isBlank()) {
            imageUrl = imageUrlParam;
        }

        return ActivityEvidence.builder()
                .id(evidence)
                .title(title)
                .content(content)
                .imageUrl(imageUrl)
                .build();
    }

    /**
     * S3에 파일을 업로드하고 URL을 반환합니다.
     * @param file 업로드할 MultipartFile
     * @return 업로드된 파일의 URL
     * @throws S3UploadFailedException 업로드 중 예외가 발생할 경우
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
     * 카테고리 이름과 회원 정보를 기반으로 새로운 Score 엔티티를 생성합니다.
     * @param categoryName 카테고리 이름
     * @param member 회원 객체
     * @return 생성된 Score 객체
     */
    private Score createScore(String categoryName, Member member) {
        Category category = categoryPersistencePort.findAllCategory()
                .stream()
                .filter(cat -> cat.getName().equals(categoryName))
                .findFirst()
                .orElseThrow(CategoryNotFoundException::new);
        return Score.builder()
                .category(category)
                .value(0)
                .member(member)
                .build();
    }
}