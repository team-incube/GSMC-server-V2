package team.incude.gsmc.v2.domain.score.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.score.application.port.CategoryPersistencePort;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.application.usecase.UpdateScoreUseCase;
import team.incude.gsmc.v2.domain.score.domain.Category;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.domain.score.exception.RequiredEvidenceCategoryException;
import team.incude.gsmc.v2.domain.score.exception.ScoreLimitExceededException;
import team.incude.gsmc.v2.global.annotation.aspect.CalculateTotalScoreStduentCode;
import team.incude.gsmc.v2.global.annotation.aspect.TriggerCalculateTotalScore;
import team.incude.gsmc.v2.global.security.jwt.usecase.service.CurrentMemberProvider;
import team.incude.gsmc.v2.global.util.ValueLimiterUtil;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateScoreService implements UpdateScoreUseCase {

    private final ScorePersistencePort scorePersistencePort;
    private final CategoryPersistencePort categoryPersistencePort;
    private final MemberPersistencePort memberPersistencePort;
    private final CurrentMemberProvider currentMemberProvider;

    @Override
    public void execute(String categoryName, Integer value) {
        updateScore(currentMemberProvider.getCurrentUser().getEmail(), categoryName, value);
    }

    @Override
    public void execute(String studentCode, String categoryName, Integer value) {
        updateScore(studentCode, categoryName, value);
    }

    @TriggerCalculateTotalScore
    private void updateScore(@CalculateTotalScoreStduentCode String studentCode, String categoryName, Integer value) {
        Category category = categoryPersistencePort.findCategoryByName(categoryName);
        if (ValueLimiterUtil.isExceedingLimit(value, category.getMaximumValue())) {
            throw new ScoreLimitExceededException();
        }
        if (category.getIsEvidenceRequired()) {
            throw new RequiredEvidenceCategoryException();
        }
        Score score = scorePersistencePort.findScoreByCategoryNameAndStudentDetailStudentCodeWithLock(categoryName, studentCode);
        if (score == null) {
            Member member = memberPersistencePort.findMemberByStudentDetailStudentCode(studentCode);
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
    }

    private Score createNewScore(Category category, Member member) {
        return Score.builder()
                .category(category)
                .member(member)
                .value(1)
                .build();
    }
}