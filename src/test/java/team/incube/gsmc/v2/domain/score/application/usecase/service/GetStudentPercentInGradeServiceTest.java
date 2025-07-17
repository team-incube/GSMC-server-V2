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
import team.incube.gsmc.v2.domain.score.domain.constant.PercentileType;
import team.incube.gsmc.v2.domain.score.exception.StudentClassMismatchException;
import team.incube.gsmc.v2.domain.score.presentation.data.response.GetStudentPercentResponse;
import team.incube.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("학생 성적 학년 백분위 조회 서비스 클래스의")
class GetStudentPercentInGradeServiceTest {

    @Mock
    private ScorePersistencePort scorePersistencePort;

    @Mock
    private StudentDetailPersistencePort studentDetailPersistencePort;

    @Mock
    private CurrentMemberProvider currentMemberProvider;

    @InjectMocks
    private GetStudentPercentInGradeService getStudentPercentInGradeService;

    @Nested
    @DisplayName("execute(PercentileType percentileType, Integer grade) 메서드는")
    class Describe_execute {

        @Nested
        @DisplayName("유효한 학년 정보가 주어졌을 때")
        class Context_with_valid_grade {

            @Test
            @DisplayName("상위 백분위를 올바르게 계산하여 반환한다")
            void it_returns_correct_top_percentile() {
                // given
                String email = "s24058@gsm.hs.kr";
                Integer grade = 2;
                Integer studentScore = 450;

                Member member = Member.builder().email(email).build();
                StudentDetail studentDetail = StudentDetail.builder()
                        .grade(grade)
                        .classNumber(4)
                        .build();

                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(studentDetailPersistencePort.findStudentDetailByEmail(email)).thenReturn(studentDetail);
                when(studentDetailPersistencePort.findTotalScoreByEmail(email)).thenReturn(studentScore);
                when(scorePersistencePort.countStudentsWithLowerScoreInGrade(studentScore, grade)).thenReturn(40L);
                when(scorePersistencePort.countTotalStudentsInGrade(grade)).thenReturn(100L);

                // when
                GetStudentPercentResponse response = getStudentPercentInGradeService.execute(PercentileType.HIGH, grade);

                // then
                assertThat(response.result()).isEqualTo(40); // (40/100) * 100 = 40%
            }

            @Test
            @DisplayName("하위 백분위를 올바르게 계산하여 반환한다")
            void it_returns_correct_bottom_percentile() {
                // given
                String email = "s24058@gsm.hs.kr";
                Integer grade = 2;
                Integer studentScore = 450;

                Member member = Member.builder().email(email).build();
                StudentDetail studentDetail = StudentDetail.builder()
                        .grade(grade)
                        .classNumber(4)
                        .build();

                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(studentDetailPersistencePort.findStudentDetailByEmail(email)).thenReturn(studentDetail);
                when(studentDetailPersistencePort.findTotalScoreByEmail(email)).thenReturn(studentScore);
                when(scorePersistencePort.countStudentsWithLowerScoreInGrade(studentScore, grade)).thenReturn(40L);
                when(scorePersistencePort.countTotalStudentsInGrade(grade)).thenReturn(100L);

                // when
                GetStudentPercentResponse response = getStudentPercentInGradeService.execute(PercentileType.LOW, grade);

                // then
                assertThat(response.result()).isEqualTo(60); // 100 - 40 = 60%
            }

            @Test
            @DisplayName("학년 1등인 경우 상위 백분위를 올바르게 계산한다")
            void it_returns_correct_percentile_for_top_student_in_grade() {
                // given
                String email = "s24058@gsm.hs.kr";
                Integer grade = 3;
                Integer studentScore = 600;

                Member member = Member.builder().email(email).build();
                StudentDetail studentDetail = StudentDetail.builder()
                        .grade(grade)
                        .classNumber(1)
                        .build();

                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(studentDetailPersistencePort.findStudentDetailByEmail(email)).thenReturn(studentDetail);
                when(studentDetailPersistencePort.findTotalScoreByEmail(email)).thenReturn(studentScore);
                when(scorePersistencePort.countStudentsWithLowerScoreInGrade(studentScore, grade)).thenReturn(79L);
                when(scorePersistencePort.countTotalStudentsInGrade(grade)).thenReturn(80L);

                // when
                GetStudentPercentResponse topResponse = getStudentPercentInGradeService.execute(PercentileType.HIGH, grade);
                GetStudentPercentResponse bottomResponse = getStudentPercentInGradeService.execute(PercentileType.LOW, grade);

                // then
                assertThat(topResponse.result()).isEqualTo(99); // (79/80) * 100 = 98.75% → 99%
                assertThat(bottomResponse.result()).isEqualTo(1); // 100 - 99 = 1%
            }

            @Test
            @DisplayName("학년 꼴등인 경우 하위 백분위를 올바르게 계산한다")
            void it_returns_correct_percentile_for_bottom_student_in_grade() {
                // given
                String email = "s24058@gsm.hs.kr";
                Integer grade = 1;
                Integer studentScore = 150;

                Member member = Member.builder().email(email).build();
                StudentDetail studentDetail = StudentDetail.builder()
                        .grade(grade)
                        .classNumber(3)
                        .build();

                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(studentDetailPersistencePort.findStudentDetailByEmail(email)).thenReturn(studentDetail);
                when(studentDetailPersistencePort.findTotalScoreByEmail(email)).thenReturn(studentScore);
                when(scorePersistencePort.countStudentsWithLowerScoreInGrade(studentScore, grade)).thenReturn(0L);
                when(scorePersistencePort.countTotalStudentsInGrade(grade)).thenReturn(90L);

                // when
                GetStudentPercentResponse topResponse = getStudentPercentInGradeService.execute(PercentileType.HIGH, grade);
                GetStudentPercentResponse bottomResponse = getStudentPercentInGradeService.execute(PercentileType.LOW, grade);

                // then
                assertThat(topResponse.result()).isEqualTo(0); // (0/90) * 100 = 0%
                assertThat(bottomResponse.result()).isEqualTo(100); // 100 - 0 = 100%
            }

            @Test
            @DisplayName("소수점 반올림 처리가 올바르게 작동한다")
            void it_handles_rounding_correctly() {
                // given
                String email = "s24058@gsm.hs.kr";
                Integer grade = 2;
                Integer studentScore = 380;

                Member member = Member.builder().email(email).build();
                StudentDetail studentDetail = StudentDetail.builder()
                        .grade(grade)
                        .classNumber(2)
                        .build();

                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(studentDetailPersistencePort.findStudentDetailByEmail(email)).thenReturn(studentDetail);
                when(studentDetailPersistencePort.findTotalScoreByEmail(email)).thenReturn(studentScore);
                when(scorePersistencePort.countStudentsWithLowerScoreInGrade(studentScore, grade)).thenReturn(33L);
                when(scorePersistencePort.countTotalStudentsInGrade(grade)).thenReturn(75L);

                // when
                GetStudentPercentResponse topResponse = getStudentPercentInGradeService.execute(PercentileType.HIGH, grade);
                GetStudentPercentResponse bottomResponse = getStudentPercentInGradeService.execute(PercentileType.LOW, grade);

                // then
                assertThat(topResponse.result()).isEqualTo(44); // (33/75) * 100 = 44%
                assertThat(bottomResponse.result()).isEqualTo(56); // 100 - 44 = 56%
            }

            @Test
            @DisplayName("중간 정도 성적인 경우 백분위를 올바르게 계산한다")
            void it_returns_correct_percentile_for_middle_student() {
                // given
                String email = "s24058@gsm.hs.kr";
                Integer grade = 2;
                Integer studentScore = 400;

                Member member = Member.builder().email(email).build();
                StudentDetail studentDetail = StudentDetail.builder()
                        .grade(grade)
                        .classNumber(2)
                        .build();

                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(studentDetailPersistencePort.findStudentDetailByEmail(email)).thenReturn(studentDetail);
                when(studentDetailPersistencePort.findTotalScoreByEmail(email)).thenReturn(studentScore);
                when(scorePersistencePort.countStudentsWithLowerScoreInGrade(studentScore, grade)).thenReturn(50L);
                when(scorePersistencePort.countTotalStudentsInGrade(grade)).thenReturn(100L);

                // when
                GetStudentPercentResponse topResponse = getStudentPercentInGradeService.execute(PercentileType.HIGH, grade);
                GetStudentPercentResponse bottomResponse = getStudentPercentInGradeService.execute(PercentileType.LOW, grade);

                // then
                assertThat(topResponse.result()).isEqualTo(50); // (50/100) * 100 = 50%
                assertThat(bottomResponse.result()).isEqualTo(50); // 100 - 50 = 50%
            }
        }

        @Nested
        @DisplayName("학년 정보가 일치하지 않을 때")
        class Context_with_mismatched_grade {

            @Test
            @DisplayName("학년이 일치하지 않으면 StudentClassMismatchException을 던진다")
            void it_throws_exception_when_grade_mismatches() {
                // given
                String email = "s24058@gsm.hs.kr";
                Integer requestGrade = 2;

                Member member = Member.builder().email(email).build();
                StudentDetail studentDetail = StudentDetail.builder()
                        .grade(3) // 다른 학년
                        .classNumber(4)
                        .build();

                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(studentDetailPersistencePort.findStudentDetailByEmail(email)).thenReturn(studentDetail);

                // when & then
                assertThatThrownBy(() -> getStudentPercentInGradeService.execute(PercentileType.HIGH, requestGrade))
                        .isInstanceOf(StudentClassMismatchException.class);
            }

            @Test
            @DisplayName("1학년 학생이 2학년 백분위를 조회하려 하면 예외를 던진다")
            void it_throws_exception_when_first_grade_student_requests_second_grade() {
                // given
                String email = "s24058@gsm.hs.kr";
                Integer requestGrade = 2;

                Member member = Member.builder().email(email).build();
                StudentDetail studentDetail = StudentDetail.builder()
                        .grade(1) // 1학년 학생
                        .classNumber(1)
                        .build();

                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(studentDetailPersistencePort.findStudentDetailByEmail(email)).thenReturn(studentDetail);

                // when & then
                assertThatThrownBy(() -> getStudentPercentInGradeService.execute(PercentileType.LOW, requestGrade))
                        .isInstanceOf(StudentClassMismatchException.class);
            }

            @Test
            @DisplayName("3학년 학생이 1학년 백분위를 조회하려 하면 예외를 던진다")
            void it_throws_exception_when_third_grade_student_requests_first_grade() {
                // given
                String email = "s24058@gsm.hs.kr";
                Integer requestGrade = 1;

                Member member = Member.builder().email(email).build();
                StudentDetail studentDetail = StudentDetail.builder()
                        .grade(3) // 3학년 학생
                        .classNumber(2)
                        .build();

                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(studentDetailPersistencePort.findStudentDetailByEmail(email)).thenReturn(studentDetail);

                // when & then
                assertThatThrownBy(() -> getStudentPercentInGradeService.execute(PercentileType.HIGH, requestGrade))
                        .isInstanceOf(StudentClassMismatchException.class);
            }
        }
    }
}