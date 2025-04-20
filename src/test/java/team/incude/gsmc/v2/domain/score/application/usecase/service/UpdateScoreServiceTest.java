package team.incude.gsmc.v2.domain.score.application.usecase.service;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.score.application.port.CategoryPersistencePort;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Category;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.global.security.jwt.usecase.service.CurrentMemberProvider;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateScoreService 클래스의")
class UpdateScoreServiceTest {

    @Mock
    private ScorePersistencePort scorePersistencePort;

    @Mock
    private CategoryPersistencePort categoryPersistencePort;

    @Mock
    private MemberPersistencePort memberPersistencePort;

    @Mock
    private CurrentMemberProvider currentMemberProvider;

    @InjectMocks
    private UpdateScoreService updateScoreService;

    @Nested
    @DisplayName("execute(String categoryName, Integer value) 메서드는")
    class Describe_executeWithoutEmail {

        @Nested
        @DisplayName("현재 로그인된 유저 정보가 주어졌을 때")
        class Context_with_authenticated_user {

            @Test
            @DisplayName("새로운 점수를 저장하거나 기존 점수를 갱신한다")
            void it_updates_or_creates_score() {
                // given
                String email = "user@gsm.hs.kr";
                String categoryName = "MAJOR-CERTIFICATE-NUM";
                int value = 3;

                Member member = Member.builder().email(email).build();
                Category category = Category.builder()
                        .name(categoryName)
                        .maximumValue(6)
                        .isEvidenceRequired(false)
                        .build();

                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(categoryPersistencePort.findCategoryByName(categoryName)).thenReturn(category);
                when(scorePersistencePort.findScoreByCategoryNameAndMemberEmail(categoryName, email)).thenReturn(null);
                when(memberPersistencePort.findMemberByEmail(email)).thenReturn(member);

                // when
                updateScoreService.execute(categoryName, value);

                // then
                verify(scorePersistencePort).saveScore(any(Score.class));
            }
        }
    }

    @Nested
    @DisplayName("execute(String email, String categoryName, Integer value) 메서드는")
    class Describe_executeWithEmail {

        @Nested
        @DisplayName("점수가 이미 존재할 때")
        class Context_with_existing_score {

            @Test
            @DisplayName("점수 값을 갱신한다")
            void it_updates_existing_score() {
                // given
                String email = "test@gsm.hs.kr";
                String categoryName = "HUMANITIES-ACTIVITIES";
                int value = 4;

                Category category = Category.builder()
                        .name(categoryName)
                        .maximumValue(5)
                        .isEvidenceRequired(false)
                        .build();

                Member member = Member.builder().email(email).build();

                Score existingScore = Score.builder()
                        .id(1L)
                        .member(member)
                        .category(category)
                        .value(2)
                        .build();

                when(categoryPersistencePort.findCategoryByName(categoryName)).thenReturn(category);
                when(scorePersistencePort.findScoreByCategoryNameAndMemberEmail(categoryName, email)).thenReturn(existingScore);

                // when
                updateScoreService.execute(email, categoryName, value);

                // then
                verify(scorePersistencePort).saveScore(argThat(score ->
                        score.getId().equals(1L) &&
                                score.getValue() == 4 &&
                                score.getCategory().getName().equals(categoryName)
                ));
            }
        }
    }
}