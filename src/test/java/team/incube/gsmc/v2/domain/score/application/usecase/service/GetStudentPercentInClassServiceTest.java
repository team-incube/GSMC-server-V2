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
@DisplayName("학생 성적 백분위 조회 서비스 클래스의")
class GetStudentPercentInClassServiceTest {

    @Mock
    private ScorePersistencePort scorePersistencePort;

    @Mock
    private StudentDetailPersistencePort studentDetailPersistencePort;

    @Mock
    private CurrentMemberProvider currentMemberProvider;

    @InjectMocks
    private GetStudentPercentInClassService getStudentPercentInClassService;

    @Nested
    @DisplayName("execute(PercentileType percentileType, Integer grade, Integer classNumber) 메서드는")
    class Describe_execute {

        @Nested
        @DisplayName("유효한 학년과 반 정보가 주어졌을 때")
        class Context_with_valid_grade_and_class {

            @Test
            @DisplayName("상위 백분위를 올바르게 계산하여 반환한다")
            void it_returns_correct_top_percentile() {
                // given
                String email = "s24058@gsm.hs.kr";
                Integer grade = 2;
                Integer classNumber = 4;
                Integer studentScore = 450;
                Member member = Member.builder().email(email).build();
                StudentDetail studentDetail = StudentDetail.builder()
                        .grade(grade)
                        .classNumber(classNumber)
                        .build();

                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(studentDetailPersistencePort.findStudentDetailByEmail(email)).thenReturn(studentDetail);
                when(studentDetailPersistencePort.findTotalScoreByEmail(email)).thenReturn(studentScore);
                when(scorePersistencePort.countStudentsWithLowerScoreInClass(studentScore, grade, classNumber)).thenReturn(7L);
                when(scorePersistencePort.countTotalStudentsInClass(grade, classNumber)).thenReturn(20L);

                // when
                GetStudentPercentResponse response = getStudentPercentInClassService.execute(PercentileType.HIGH, grade, classNumber);

                // then
                assertThat(response.result()).isEqualTo(35); // (7/20) * 100 = 35%
            }

            @Test
            @DisplayName("하위 백분위를 올바르게 계산하여 반환한다")
            void it_returns_correct_bottom_percentile() {
                // given
                String email = "s24058@gsm.hs.kr";
                Integer grade = 2;
                Integer classNumber = 4;
                Integer studentScore = 450;

                Member member = Member.builder().email(email).build();
                StudentDetail studentDetail = StudentDetail.builder()
                        .grade(grade)
                        .classNumber(classNumber)
                        .build();
                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(studentDetailPersistencePort.findStudentDetailByEmail(email)).thenReturn(studentDetail);
                when(studentDetailPersistencePort.findTotalScoreByEmail(email)).thenReturn(studentScore);
                when(scorePersistencePort.countStudentsWithLowerScoreInClass(studentScore, grade, classNumber)).thenReturn(7L);
                when(scorePersistencePort.countTotalStudentsInClass(grade, classNumber)).thenReturn(20L);

                // when
                GetStudentPercentResponse response = getStudentPercentInClassService.execute(PercentileType.LOW, grade, classNumber);

                // then
                assertThat(response.result()).isEqualTo(65); // 100 - 35 = 65%
            }

            @Test
            @DisplayName("1등인 경우 상위 100%, 하위 0%를 반환한다")
            void it_returns_correct_percentile_for_top_student() {
                // given
                String email = "s24058@gsm.hs.kr";
                Integer grade = 2;
                Integer classNumber = 4;
                Integer studentScore = 600;
                Member member = Member.builder().email(email).build();
                StudentDetail studentDetail = StudentDetail.builder()
                        .grade(grade)
                        .classNumber(classNumber)
                        .build();
                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(studentDetailPersistencePort.findStudentDetailByEmail(email)).thenReturn(studentDetail);
                when(studentDetailPersistencePort.findTotalScoreByEmail(email)).thenReturn(studentScore);
                when(scorePersistencePort.countStudentsWithLowerScoreInClass(studentScore, grade, classNumber)).thenReturn(19L);
                when(scorePersistencePort.countTotalStudentsInClass(grade, classNumber)).thenReturn(20L);

                // when
                GetStudentPercentResponse topResponse = getStudentPercentInClassService.execute(PercentileType.HIGH, grade, classNumber);
                GetStudentPercentResponse bottomResponse = getStudentPercentInClassService.execute(PercentileType.LOW, grade, classNumber);

                // then
                assertThat(topResponse.result()).isEqualTo(95); // (19/20) * 100 = 95%
                assertThat(bottomResponse.result()).isEqualTo(5); // 100 - 95 = 5%
            }

            @Test
            @DisplayName("꼴등인 경우 상위 0%, 하위 100%를 반환한다")
            void it_returns_correct_percentile_for_bottom_student() {
                // given
                String email = "s24058@gsm.hs.kr";
                Integer grade = 2;
                Integer classNumber = 4;
                Integer studentScore = 100;
                Member member = Member.builder().email(email).build();
                StudentDetail studentDetail = StudentDetail.builder()
                        .grade(grade)
                        .classNumber(classNumber)
                        .build();
                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(studentDetailPersistencePort.findStudentDetailByEmail(email)).thenReturn(studentDetail);
                when(studentDetailPersistencePort.findTotalScoreByEmail(email)).thenReturn(studentScore);
                when(scorePersistencePort.countStudentsWithLowerScoreInClass(studentScore, grade, classNumber)).thenReturn(0L);
                when(scorePersistencePort.countTotalStudentsInClass(grade, classNumber)).thenReturn(20L);

                // when
                GetStudentPercentResponse topResponse = getStudentPercentInClassService.execute(PercentileType.HIGH, grade, classNumber);
                GetStudentPercentResponse bottomResponse = getStudentPercentInClassService.execute(PercentileType.LOW, grade, classNumber);

                // then
                assertThat(topResponse.result()).isEqualTo(0); // (0/20) * 100 = 0%
                assertThat(bottomResponse.result()).isEqualTo(100); // 100 - 0 = 100%
            }

            @Test
            @DisplayName("반올림 처리가 올바르게 작동한다")
            void it_handles_rounding_correctly() {
                // given
                String email = "s24058@gsm.hs.kr";
                Integer grade = 2;
                Integer classNumber = 4;
                Integer studentScore = 450;
                Member member = Member.builder().email(email).build();
                StudentDetail studentDetail = StudentDetail.builder()
                        .grade(grade)
                        .classNumber(classNumber)
                        .build();
                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(studentDetailPersistencePort.findStudentDetailByEmail(email)).thenReturn(studentDetail);
                when(studentDetailPersistencePort.findTotalScoreByEmail(email)).thenReturn(studentScore);
                when(scorePersistencePort.countStudentsWithLowerScoreInClass(studentScore, grade, classNumber)).thenReturn(1L);
                when(scorePersistencePort.countTotalStudentsInClass(grade, classNumber)).thenReturn(3L);

                // when
                GetStudentPercentResponse response = getStudentPercentInClassService.execute(PercentileType.HIGH, grade, classNumber);

                // then
                assertThat(response.result()).isEqualTo(33); // (1/3) * 100 = 33.333... → 33%
            }
        }

        @Nested
        @DisplayName("학년 또는 반 정보가 일치하지 않을 때")
        class Context_with_mismatched_grade_or_class {

            @Test
            @DisplayName("학년이 일치하지 않으면 StudentClassMismatchException을 던진다")
            void it_throws_exception_when_grade_mismatches() {
                // given
                String email = "s24058@gsm.hs.kr";
                Integer requestGrade = 2;
                Integer requestClassNumber = 4;
                Member member = Member.builder().email(email).build();
                StudentDetail studentDetail = StudentDetail.builder()
                        .grade(3)
                        .classNumber(requestClassNumber)
                        .build();
                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(studentDetailPersistencePort.findStudentDetailByEmail(email)).thenReturn(studentDetail);

                // when & then
                assertThatThrownBy(() -> getStudentPercentInClassService.execute(PercentileType.HIGH, requestGrade, requestClassNumber))
                        .isInstanceOf(StudentClassMismatchException.class);
            }

            @Test
            @DisplayName("반이 일치하지 않으면 StudentClassMismatchException을 던진다")
            void it_throws_exception_when_class_mismatches() {
                // given
                String email = "s24058@gsm.hs.kr";
                Integer requestGrade = 2;
                Integer requestClassNumber = 4;
                Member member = Member.builder().email(email).build();
                StudentDetail studentDetail = StudentDetail.builder()
                        .grade(requestGrade)
                        .classNumber(5)
                        .build();
                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(studentDetailPersistencePort.findStudentDetailByEmail(email)).thenReturn(studentDetail);

                // when & then
                assertThatThrownBy(() -> getStudentPercentInClassService.execute(PercentileType.HIGH, requestGrade, requestClassNumber))
                        .isInstanceOf(StudentClassMismatchException.class);
            }
        }
    }
}