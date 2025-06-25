package team.incube.gsmc.v2.domain.score.application.usecase.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.incube.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incube.gsmc.v2.domain.member.domain.Member;
import team.incube.gsmc.v2.domain.member.domain.StudentDetail;
import team.incube.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incube.gsmc.v2.domain.score.domain.Category;
import team.incube.gsmc.v2.domain.score.domain.Score;
import team.incube.gsmc.v2.domain.score.presentation.data.GetScoreDto;
import team.incube.gsmc.v2.domain.score.presentation.data.response.GetScoreResponse;
import team.incube.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("인증제 점수 조회 서비스 클래스의")
class FindScoreServiceTest {

    @Mock
    private ScorePersistencePort scorePersistencePort;

    @Mock
    private StudentDetailPersistencePort studentDetailPersistencePort;

    @Mock
    private CurrentMemberProvider currentMemberProvider;

    @InjectMocks
    private FindScoreService findScoreService;

    @Nested
    @DisplayName("execute(String email) 메서드는")
    class Describe_executeWithEmail {

        @Nested
        @DisplayName("유효한 email 주어졌을 때")
        class Context_with_valid_email {

            @Test
            @DisplayName("해당 사용자의 점수 목록과 총점을 반환한다")
            void it_returns_scores_and_total_score() {
                // given
                String email = "s24058@gsm.hs.kr";
                List<Score> scores = List.of(
                        Score.builder()
                                .category(Category.builder().name("MAJOR-CERTIFICATE-NUM").build())
                                .value(5)
                                .build(),
                        Score.builder()
                                .category(Category.builder().name("HUMANITIES-CERTIFICATE-KOREAN-HISTORY").build())
                                .value(2)
                                .build()
                );
                when(scorePersistencePort.findScoreByMemberEmail(email)).thenReturn(scores);
                when(studentDetailPersistencePort.findTotalScoreByEmail(email)).thenReturn(95);

                // when
                GetScoreResponse response = findScoreService.execute(email);

                // then
                assertThat(response.totalScore()).isEqualTo(95);
                assertThat(response.scores()).hasSize(2);
                assertThat(response.scores())
                        .extracting(GetScoreDto::categoryName)
                        .containsExactly("MAJOR-CERTIFICATE-NUM", "HUMANITIES-CERTIFICATE-KOREAN-HISTORY");
                assertThat(response.scores())
                        .extracting(GetScoreDto::value)
                        .containsExactly(5, 2);
            }
        }
    }

    @Nested
    @DisplayName("execute() 메서드는")
    class Describe_executeWithoutStudentCode {

        @Nested
        @DisplayName("현재 로그인된 사용자가 존재할 때")
        class Context_with_authenticated_user {

            @Test
            @DisplayName("해당 사용자의 점수 목록과 총점을 반환한다")
            void it_returns_scores_and_total_score_of_current_user() {
                // given
                String studentCode = "24058";
                String email = "current@gsm.hs.kr";
                Member member = Member.builder().email(email).build();
                StudentDetail studentDetail = StudentDetail.builder()
                        .studentCode(studentCode)
                        .member(member)
                        .build();
                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(scorePersistencePort.findScoreByMemberEmail(email)).thenReturn(List.of());
                when(studentDetailPersistencePort.findTotalScoreByEmail(email)).thenReturn(0);

                // when
                GetScoreResponse response = findScoreService.execute();

                // then
                assertThat(response.totalScore()).isEqualTo(0);
                assertThat(response.scores()).isEmpty();
            }
        }
    }
}