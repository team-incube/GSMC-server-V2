package team.incube.gsmc.v2.domain.score.application.usecase.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.incube.gsmc.v2.domain.score.application.port.CategoryPersistencePort;
import team.incube.gsmc.v2.domain.score.domain.Category;
import team.incube.gsmc.v2.domain.score.presentation.data.response.GetScoreSimulateResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("모의 점수 산출 서비스 클래스의")
class SimulateScoreServiceTest {

    @Mock
    private CategoryPersistencePort categoryPersistencePort;

    @InjectMocks
    private SimulateScoreService simulateScoreService;

    @Nested
    @DisplayName("execute 메서드는")
    class Describe_execute {

        @Nested
        @DisplayName("올바른 파라미터가 주어지면")
        class Context_with_valid_parameters {

            @Test
            @DisplayName("모의 점수를 계산하여 반환한다")
            void it_returns_simulated_score() {
                // given
                when(categoryPersistencePort.findAllCategory()).thenReturn(List.of(
                        Category.builder().name("MAJOR_AWARD_CAREER_OUT_SCHOOL_OFFICIAL").weight(5f).build(),
                        Category.builder().name("MAJOR_CERTIFICATE_NUM").weight(10f).build(),
                        Category.builder().name("HUMANITIES_READING").weight(5f).build(),
                        Category.builder().name("MAJOR_TOPCIT_SCORE").weight(2f).build(),
                        Category.builder().name("MAJOR_CLUB_ATTENDANCE_SEMESTER1").weight(1f).build(),
                        Category.builder().name("MAJOR_OUT_SCHOOL_ATTENDANCE_HACKATHON").weight(3f).build(),
                        Category.builder().name("MAJOR_IN_SCHOOL_ATTENDANCE_HACKATHON").weight(2f).build(),
                        Category.builder().name("MAJOR_IN_SCHOOL_ATTENDANCE_SEMINAR").weight(2f).build(),
                        Category.builder().name("MAJOR_IN_SCHOOL_ATTENDANCE_AFTER_SCHOOL").weight(2f).build(),
                        Category.builder().name("HUMANITIES_AWARD_CAREER_HUMANITY_IN_SCHOOL").weight(3f).build(),
                        Category.builder().name("HUMANITIES_READING_READ_A_THON_TURTLE").weight(1f).build(),
                        Category.builder().name("HUMANITIES_READING_READ_A_THON_CROCODILE").weight(1f).build(),
                        Category.builder().name("HUMANITIES_READING_READ_A_THON_RABBIT_OVER").weight(1f).build(),
                        Category.builder().name("HUMANITIES_SERVICE_ACTIVITY").weight(2f).build(),
                        Category.builder().name("HUMANITIES_SERVICE_CLUB_SEMESTER1").weight(2f).build(),
                        Category.builder().name("HUMANITIES_CERTIFICATE_CHINESE_CHARACTER").weight(1f).build(),
                        Category.builder().name("HUMANITIES_CERTIFICATE_KOREAN_HISTORY").weight(1f).build(),
                        Category.builder().name("HUMANITIES_ACTIVITIES_SELF_DIRECTED_ACTIVITIES").weight(2f).build(),
                        Category.builder().name("HUMANITIES_ACTIVITIES_NEWRROW_S").weight(2f).build()
                ));

                // when
                GetScoreSimulateResponse response = simulateScoreService.execute(
                        1, 1, 1, 1, 1, 1, // major awards
                        2, 3, 1, 1, // cert, topcit, club
                        1, 1, 1, 1, 1, 1, 1, 1, // major attendance
                        1, 1, // humanities awards
                        1, false, false, // humanities reading
                        true, 1, 1, // humanities service
                        1, 1, // humanities certs
                        true, true, // humanities activities
                        0, 800, true, 300, 7, 3, 500, 700, 6, 0, 0 // lang
                );

                // then
                assertThat(response).isNotNull();
                assertThat(response.totalScore()).isInstanceOf(Integer.class);
            }
        }
    }
}