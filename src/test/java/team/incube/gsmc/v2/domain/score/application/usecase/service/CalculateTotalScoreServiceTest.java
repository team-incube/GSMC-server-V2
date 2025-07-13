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
import team.incube.gsmc.v2.domain.score.application.port.CategoryPersistencePort;
import team.incube.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incube.gsmc.v2.domain.score.domain.Category;
import team.incube.gsmc.v2.domain.score.domain.Score;

import java.util.List;
import java.util.stream.LongStream;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("총점 계산 서비스 클래스의")
class CalculateTotalScoreServiceTest {

    @Mock
    private ScorePersistencePort scorePersistencePort;

    @Mock
    private CategoryPersistencePort categoryPersistencePort;

    @Mock
    private StudentDetailPersistencePort studentDetailPersistencePort;

    @InjectMocks
    private CalculateTotalScoreService calculateTotalScoreService;

    @Nested
    @DisplayName("execute 메서드는")
    class Describe_execute {

        @Test
        @DisplayName("점수를 계산하고 학생 정보를 저장한다")
        void it_calculates_and_saves_total_score() {
            // given
            String studentCode = "24058";
            List<Score> scores = LongStream.rangeClosed(1, 41)
                    .mapToObj(i -> Score.builder()
                            .category(Category.builder().id(i).build())
                            .value(1)
                            .build())
                    .toList();
            Member member = Member.builder()
                    .email("s24058@gsm.hs.kr")
                    .id(1L)
                    .build();
            StudentDetail studentDetail = StudentDetail.builder()
                    .id(1L)
                    .studentCode(studentCode)
                    .member(member)
                    .classNumber(1)
                    .grade(2)
                    .number(3)
                    .build();
            when(scorePersistencePort.findScoreByMemberEmail(member.getEmail())).thenReturn(scores);
            when(categoryPersistencePort.findAllCategory()).thenReturn(List.of(
                    Category.builder().id(1L).name("MAJOR-AWARD_CAREER-OUT_SCHOOL-OFFICIAL").weight(50f).build(),
                    Category.builder().id(2L).name("MAJOR-AWARD_CAREER-OUT_SCHOOL-UNOFFICIAL").weight(50f).build(),
                    Category.builder().id(3L).name("MAJOR-AWARD_CAREER-OUT_SCHOOL-HACKATHON").weight(50f).build(),
                    Category.builder().id(4L).name("MAJOR-AWARD_CAREER-IN_SCHOOL-GSMFEST").weight(50f).build(),
                    Category.builder().id(5L).name("MAJOR-AWARD_CAREER-IN_SCHOOL-SCHOOL_HACKATHON").weight(50f).build(),
                    Category.builder().id(6L).name("MAJOR-AWARD_CAREER-IN_SCHOOL-PRESENTATION").weight(50f).build(),
                    Category.builder().id(7L).name("MAJOR-CERTIFICATE_NUM").weight(50f).build(),
                    Category.builder().id(8L).name("MAJOR-TOPCIT_SCORE").weight(3.3f).build(),
                    Category.builder().id(9L).name("MAJOR-CLUB_ATTENDANCE_SEMESTER_1").weight(50f).build(),
                    Category.builder().id(10L).name("MAJOR-CLUB_ATTENDANCE_SEMESTER_2").weight(50f).build(),
                    Category.builder().id(11L).name("MAJOR-OUT_SCHOOL-ATTENDANCE_OFFICIAL_CONTEST").weight(25f).build(),
                    Category.builder().id(12L).name("MAJOR-OUT_SCHOOL-ATTENDANCE_UNOFFICIAL_CONTEST").weight(25f).build(),
                    Category.builder().id(13L).name("MAJOR-OUT_SCHOOL-ATTENDANCE_HACKATHON").weight(25f).build(),
                    Category.builder().id(14L).name("MAJOR-OUT_SCHOOL-ATTENDANCE_SEMINAR").weight(25f).build(),
                    Category.builder().id(15L).name("MAJOR-IN_SCHOOL-ATTENDANCE_GSMFEST").weight(50f).build(),
                    Category.builder().id(16L).name("MAJOR-IN_SCHOOL-ATTENDANCE_HACKATHON").weight(50f).build(),
                    Category.builder().id(17L).name("MAJOR-IN_SCHOOL-ATTENDANCE_CLUB-PRESENTATION").weight(50f).build(),
                    Category.builder().id(18L).name("MAJOR-IN_SCHOOL-ATTENDANCE_SEMINAR").weight(15f).build(),
                    Category.builder().id(19L).name("MAJOR-IN_SCHOOL-ATTENDANCE_AFTER-SCHOOL").weight(45f).build(),
                    Category.builder().id(20L).name("HUMANITIES-AWARD_CAREER-HUMANITY-IN_SCHOOL").weight(50f).build(),
                    Category.builder().id(21L).name("HUMANITIES-AWARD_CAREER-HUMANITY-OUT_SCHOOL").weight(50f).build(),
                    Category.builder().id(22L).name("HUMANITIES-READING-READ_A_THON-TURTLE").weight(40f).build(),
                    Category.builder().id(23L).name("HUMANITIES-READING-READ_A_THON-CROCODILE").weight(70f).build(),
                    Category.builder().id(24L).name("HUMANITIES-READING-READ_A_THON-RABBIT_OVER").weight(100f).build(),
                    Category.builder().id(25L).name("HUMANITIES-READING").weight(10f).build(),
                    Category.builder().id(26L).name("HUMANITIES-SERVICE-ACTIVITY").weight(5f).build(),
                    Category.builder().id(27L).name("HUMANITIES-SERVICE-CLUB_SEMESTER_1").weight(50f).build(),
                    Category.builder().id(28L).name("HUMANITIES-SERVICE-CLUB_SEMESTER_2").weight(50f).build(),
                    Category.builder().id(29L).name("HUMANITIES-CERTIFICATE-CHINESE_CHARACTER").weight(50f).build(),
                    Category.builder().id(30L).name("HUMANITIES-CERTIFICATE-KOREAN_HISTORY").weight(50f).build(),
                    Category.builder().id(31L).name("HUMANITIES-ACTIVITIES-NEWRROW_S").weight(5f).build(),
                    Category.builder().id(32L).name("HUMANITIES-ACTIVITIES-SELF-DIRECTED_ACTIVITIES").weight(25f).build(),
                    Category.builder().id(33L).name("FOREIGN_LANG-ATTENDANCE-TOEIC_ACADMY_STATUS").weight(100f).build(),
                    Category.builder().id(34L).name("FOREIGN_LANG-TOEIC_SCORE").weight(-1f).build(),
                    Category.builder().id(35L).name("FOREIGN_LANG-TOEFL_SCORE").weight(-1f).build(),
                    Category.builder().id(36L).name("FOREIGN_LANG-TEPS_SCORE").weight(-1f).build(),
                    Category.builder().id(37L).name("FOREIGN_LANG-TOEIC_SPEAKING_LEVEL").weight(-1f).build(),
                    Category.builder().id(38L).name("FOREIGN_LANG-OPIC_GRADE").weight(-1f).build(),
                    Category.builder().id(39L).name("FOREIGN_LANG-JPT_SCORE").weight(-1f).build(),
                    Category.builder().id(40L).name("FOREIGN_LANG-CPT_SCORE").weight(-1f).build(),
                    Category.builder().id(41L).name("FOREIGN_LANG-HSK_GRADE").weight(-1f).build()
            ));
            when(studentDetailPersistencePort.findStudentDetailByEmail(member.getEmail())).thenReturn(studentDetail);

            // when
            calculateTotalScoreService.execute(member.getEmail());

            // then
            verify(studentDetailPersistencePort).saveStudentDetail(any(StudentDetail.class));
        }
    }
}