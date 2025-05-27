package team.incude.gsmc.v2.domain.score.application.usecase.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.member.domain.StudentDetail;
import team.incude.gsmc.v2.domain.score.application.port.CategoryPersistencePort;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Category;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.domain.score.exception.CategoryNotFoundException;
import team.incude.gsmc.v2.global.event.ScoreUpdatedEvent;
import team.incude.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("인증제 점수 갱신 서비스 클래스의")
class UpdateScoreServiceTest {

    @Mock
    private ScorePersistencePort scorePersistencePort;
    @Mock
    private CategoryPersistencePort categoryPersistencePort;
    @Mock
    private MemberPersistencePort memberPersistencePort;
    @Mock
    private StudentDetailPersistencePort studentDetailPersistencePort;
    @Mock
    private CurrentMemberProvider currentMemberProvider;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;
    @InjectMocks
    private UpdateScoreService updateScoreService;

    @Nested
    @DisplayName("execute(String categoryName, Integer value) 메서드는")
    class Describe_executeWithoutStudentCode {

        @Nested
        @DisplayName("현재 로그인된 유저의 점수가 존재하지 않으면")
        class Context_when_score_not_exists {

            @Test
            @DisplayName("새로운 점수를 생성한다")
            void it_creates_new_score() {
                // given
                String email = "user@gsm.hs.kr";
                String studentCode = "24058";
                String categoryName = "MAJOR-CERTIFICATE-NUM";
                int value = 3;
                Member member = Member.builder().email(email).build();
                Category category = Category.builder()
                        .name(categoryName)
                        .maximumValue(6)
                        .isEvidenceRequired(false)
                        .build();
                StudentDetail studentDetail = StudentDetail.builder()
                        .studentCode(studentCode)
                        .member(member)
                        .build();
                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(studentDetailPersistencePort.findStudentDetailByMemberEmail(email)).thenReturn(studentDetail);
                when(categoryPersistencePort.findAllCategory()).thenReturn(List.of(category));
                when(scorePersistencePort.findScoreByCategoryNameAndStudentDetailStudentCodeWithLock(categoryName, studentCode)).thenReturn(null);
                when(memberPersistencePort.findMemberByStudentDetailStudentCode(studentCode)).thenReturn(member);

                // when
                updateScoreService.execute(categoryName, value);

                // then
                verify(scorePersistencePort).saveScore(any(Score.class));
                verify(applicationEventPublisher).publishEvent(argThat((Object event) ->
                        event instanceof ScoreUpdatedEvent &&
                                ((ScoreUpdatedEvent) event).studentCode().equals(studentCode)
                ));
            }
        }

        @Nested
        @DisplayName("현재 로그인된 유저의 점수가 이미 존재하면")
        class Context_when_score_exists {

            @Test
            @DisplayName("기존 점수를 갱신한다")
            void it_updates_existing_score() {
                // given
                String email = "user@gsm.hs.kr";
                String studentCode = "24058";
                String categoryName = "MAJOR-CERTIFICATE-NUM";
                int value = 5;

                Member member = Member.builder().email(email).build();
                Category category = Category.builder()
                        .name(categoryName)
                        .maximumValue(6)
                        .isEvidenceRequired(false)
                        .build();
                StudentDetail studentDetail = StudentDetail.builder()
                        .studentCode(studentCode)
                        .member(member)
                        .build();
                Score existingScore = Score.builder()
                        .id(1L)
                        .member(member)
                        .category(category)
                        .value(2)
                        .build();
                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(studentDetailPersistencePort.findStudentDetailByMemberEmail(email)).thenReturn(studentDetail);
                when(categoryPersistencePort.findAllCategory()).thenReturn(List.of(category));
                when(scorePersistencePort.findScoreByCategoryNameAndStudentDetailStudentCodeWithLock(categoryName, studentCode)).thenReturn(existingScore);

                // when
                updateScoreService.execute(categoryName, value);

                // then
                verify(scorePersistencePort).saveScore(argThat(score ->
                        score.getId().equals(1L) &&
                                score.getValue() == value &&
                                score.getCategory().getName().equals(categoryName)
                ));
                verify(applicationEventPublisher).publishEvent(argThat((Object event) ->
                        event instanceof ScoreUpdatedEvent &&
                                ((ScoreUpdatedEvent) event).studentCode().equals(studentCode)
                ));
            }
        }
    }

    @Nested
    @DisplayName("execute(String studentCode, String categoryName, Integer value) 메서드는")
    class Describe_executeWithStudentCode {

        @Nested
        @DisplayName("점수가 존재하지 않으면")
        class Context_when_score_not_exists {

            @Test
            @DisplayName("새로운 점수를 생성한다")
            void it_creates_score() {
                // given
                String studentCode = "24058";
                String categoryName = "HUMANITIES-READING";
                int value = 1;
                Member member = Member.builder().id(1L).build();
                Category category = Category.builder()
                        .name(categoryName)
                        .maximumValue(5)
                        .isEvidenceRequired(false)
                        .build();
                when(categoryPersistencePort.findAllCategory()).thenReturn(List.of(category));
                when(scorePersistencePort.findScoreByCategoryNameAndStudentDetailStudentCodeWithLock(categoryName, studentCode)).thenReturn(null);
                when(memberPersistencePort.findMemberByStudentDetailStudentCode(studentCode)).thenReturn(member);

                // when
                updateScoreService.execute(studentCode, categoryName, value);

                // then
                verify(scorePersistencePort).saveScore(argThat(score ->
                        score.getId() == null &&
                                score.getValue() == 1 &&
                                score.getCategory().getName().equals(categoryName)
                ));
                verify(applicationEventPublisher).publishEvent(argThat((Object event) ->
                        event instanceof ScoreUpdatedEvent &&
                                ((ScoreUpdatedEvent) event).studentCode().equals(studentCode)
                ));
            }
        }

        @Nested
        @DisplayName("점수가 이미 존재하면")
        class Context_when_score_exists {

            @Test
            @DisplayName("점수를 갱신한다")
            void it_updates_score() {
                // given
                String studentCode = "24058";
                String categoryName = "HUMANITIES-READING";
                int value = 4;
                Member member = Member.builder().id(2L).build();
                Category category = Category.builder()
                        .name(categoryName)
                        .maximumValue(5)
                        .isEvidenceRequired(false)
                        .build();
                Score existingScore = Score.builder()
                        .id(2L)
                        .member(member)
                        .category(category)
                        .value(1)
                        .build();
                when(categoryPersistencePort.findAllCategory()).thenReturn(List.of(category));
                when(scorePersistencePort.findScoreByCategoryNameAndStudentDetailStudentCodeWithLock(categoryName, studentCode)).thenReturn(existingScore);

                // when
                updateScoreService.execute(studentCode, categoryName, value);

                // then
                verify(scorePersistencePort).saveScore(argThat(score ->
                        score.getId().equals(2L) &&
                                score.getValue() == value &&
                                score.getCategory().getName().equals(categoryName)
                ));
                verify(applicationEventPublisher).publishEvent(argThat((Object event) ->
                        event instanceof ScoreUpdatedEvent &&
                                ((ScoreUpdatedEvent) event).studentCode().equals(studentCode)
                ));
            }
        }

        @Nested
        @DisplayName("존재하지 않는 카테고리 이름이 주어지면")
        class Context_when_category_not_exists {

            @Test
            @DisplayName("예외를 발생시킨다")
            void it_throws_exception() {
                // given
                String studentCode = "24058";
                String categoryName = "NON_EXISTENT_CATEGORY";
                int value = 3;
                when(categoryPersistencePort.findAllCategory()).thenReturn(List.of());

                // when & then
                assertThrows(CategoryNotFoundException.class, () -> {
                    updateScoreService.execute(studentCode, categoryName, value);
                });
            }
        }
    }
}