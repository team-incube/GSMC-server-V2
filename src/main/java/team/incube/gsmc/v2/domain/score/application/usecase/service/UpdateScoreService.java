package team.incube.gsmc.v2.domain.score.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incube.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incube.gsmc.v2.domain.member.domain.Member;
import team.incube.gsmc.v2.domain.score.application.port.CategoryPersistencePort;
import team.incube.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incube.gsmc.v2.domain.score.application.usecase.UpdateScoreUseCase;
import team.incube.gsmc.v2.domain.score.domain.Category;
import team.incube.gsmc.v2.domain.score.domain.Score;
import team.incube.gsmc.v2.domain.score.exception.CategoryNotFoundException;
import team.incube.gsmc.v2.domain.score.exception.RequiredEvidenceCategoryException;
import team.incube.gsmc.v2.domain.score.exception.ScoreLimitExceededException;
import team.incube.gsmc.v2.global.event.ScoreUpdatedEvent;
import team.incube.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;
import team.incube.gsmc.v2.global.util.ValueLimiterUtil;

/**
 * 점수 수정 유스케이스의 구현체입니다.
 *
 * <p>{@link UpdateScoreUseCase}를 구현하며, 현재 사용자 또는 특정 학생의 점수를 수정할 수 있도록 처리합니다.
 *
 * <p>주요 기능:
 * <ul>
 *   <li>카테고리 최대값 검증</li>
 *   <li>증빙 필수 여부 확인</li>
 *   <li>점수 존재 시 갱신, 없을 경우 새로 생성</li>
 *   <li>점수 저장 및 {@link ScoreUpdatedEvent} 발행</li>
 * </ul>
 *
 * @author snowykte0426
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UpdateScoreService implements UpdateScoreUseCase {

    private final ScorePersistencePort scorePersistencePort;
    private final CategoryPersistencePort categoryPersistencePort;
    private final MemberPersistencePort memberPersistencePort;
    private final StudentDetailPersistencePort studentDetailPersistencePort;
    private final CurrentMemberProvider currentMemberProvider;
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 현재 로그인한 사용자의 특정 카테고리 점수를 수정합니다.
     * @param categoryName 수정할 점수의 카테고리 이름
     * @param value 설정할 점수 값
     */
    @Override
    public void execute(String categoryName, Integer value) {
        updateScore(currentMemberProvider.getCurrentUser().getEmail(), categoryName, value);
    }

    /**
     * 특정 학생의 특정 카테고리 점수를 수정합니다.
     * @param studentCode 학생 고유 코드
     * @param categoryName 수정할 점수의 카테고리 이름
     * @param value 설정할 점수 값
     */
    @Override
    public void execute(String studentCode, String categoryName, Integer value) {
        updateScore(studentCode, categoryName, value);
    }

    /**
     * 점수 값을 검증하고 저장하거나 수정합니다.
     * <p>최대 점수 제한 및 증빙 필요 여부를 확인하고,
     * 점수가 없으면 새로 생성하며, 있으면 기존 점수를 수정합니다.
     * 저장 후 {@link ScoreUpdatedEvent}를 발행합니다.
     * @param email 학생 고유 코드
     * @param categoryName 카테고리 이름
     * @param value 설정할 점수 값
     * @throws ScoreLimitExceededException 최대 점수를 초과한 경우
     * @throws RequiredEvidenceCategoryException 증빙이 필요한 카테고리인 경우
     */
    protected void updateScore(String email, String categoryName, Integer value) {
        Category category = categoryPersistencePort.findAllCategory()
                .stream()
                .filter(cat -> cat.getName().equals(categoryName))
                .findFirst()
                .orElseThrow(CategoryNotFoundException::new);
        if (ValueLimiterUtil.isExceedingLimit(value, category.getMaximumValue())) {
            throw new ScoreLimitExceededException();
        }
        if (category.getIsEvidenceRequired()) {
            throw new RequiredEvidenceCategoryException();
        }
        Score score = scorePersistencePort.findScoreByCategoryNameAndMemberEmailWithLock(categoryName, email);
        if (score == null) {
            Member member = memberPersistencePort.findMemberByEmail(email);
            score = createNewScore(category, member);
        } else {
            score = Score.builder()
                    .id(score.getId())
                    .member(score.getMember())
                    .category(category)
                    .value(value)
                    .build();
        }
        scorePersistencePort.saveScore(score);
        applicationEventPublisher.publishEvent(new ScoreUpdatedEvent(email));
    }

    /**
     * 새로운 점수 객체를 생성합니다.
     * @param category 점수 카테고리
     * @param member 점수를 부여할 사용자
     * @return 생성된 점수 객체
     */
    private Score createNewScore(Category category, Member member) {
        return Score.builder()
                .category(category)
                .member(member)
                .value(1)
                .build();
    }
}