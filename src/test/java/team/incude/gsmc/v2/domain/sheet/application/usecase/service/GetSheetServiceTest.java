package team.incude.gsmc.v2.domain.sheet.application.usecase.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.member.domain.StudentDetail;
import team.incude.gsmc.v2.domain.score.application.port.CategoryPersistencePort;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Category;
import team.incude.gsmc.v2.domain.score.domain.Score;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("학급 성적 시트 서비스 클래스의")
class GetSheetServiceTest {

    @Mock
    private ScorePersistencePort scorePersistencePort;
    @Mock
    private CategoryPersistencePort categoryPersistencePort;
    @Mock
    private StudentDetailPersistencePort studentDetailPersistencePort;

    @InjectMocks
    private GetSheetService getSheetService;

    @Nested
    @DisplayName("execute 메서드는")
    class Describe_execute {

        @Test
        @DisplayName("엑셀 파일을 반환한다")
        void it_returns_excel_file() {
            // given
            Integer grade = 2;
            Integer classNumber = 1;
            Member member = Member.builder()
                    .id(1L)
                    .name("홍길동")
                    .build();
            StudentDetail studentDetail = StudentDetail.builder()
                    .id(1L)
                    .studentCode("24001")
                    .grade(grade)
                    .classNumber(classNumber)
                    .number(1)
                    .totalScore(10)
                    .member(member)
                    .build();
            List<StudentDetail> studentDetails = List.of(studentDetail);
            List<Category> categories = List.of(
                    Category.builder().id(1L).name("MAJOR-AWARD_CAREER-OUT_SCHOOL-OFFICIAL").weight(50f).koreanName("전공 영역-수상경력-교외-공문을 통한 전공분야 대회").build(),
                    Category.builder().id(2L).name("MAJOR-AWARD_CAREER-OUT_SCHOOL-UNOFFICIAL").weight(50f).koreanName("전공 영역-수상경력-교외-전공 분야 대회 개별 참여").build(),
                    Category.builder().id(3L).name("MAJOR-AWARD_CAREER-OUT_SCHOOL-HACKATHON").weight(50f).koreanName("전공 영역-수상경력-교외-연합 해커톤").build(),
                    Category.builder().id(4L).name("MAJOR-AWARD_CAREER-IN_SCHOOL-GSMFEST").weight(50f).koreanName("전공 영역-수상경력-교내-GSM Festival").build(),
                    Category.builder().id(5L).name("MAJOR-AWARD_CAREER-IN_SCHOOL-SCHOOL_HACKATHON").weight(50f).koreanName("전공 영역-수상경력-교내-교내해커톤").build(),
                    Category.builder().id(6L).name("MAJOR-AWARD_CAREER-IN_SCHOOL-PRESENTATION").weight(50f).koreanName("전공 영역-수상경력-교내-전공동아리 발표").build(),
                    Category.builder().id(7L).name("MAJOR-CERTIFICATE_NUM").weight(50f).koreanName("전공영역-자격증-자격증 취득 갯수").build(),
                    Category.builder().id(8L).name("MAJOR-TOPCIT_SCORE").weight(3.3f).koreanName("전공영역-TOPCIT-취득점수").build(),
                    Category.builder().id(9L).name("MAJOR-CLUB_ATTENDANCE_SEMESTER_1").weight(50f).koreanName("전공영역-동아리-전공(심화), 취업동아리 참여 실적 ※학기별 최대 2개까지 인정").build(),
                    Category.builder().id(10L).name("MAJOR-CLUB_ATTENDANCE_SEMESTER_2").weight(50f).koreanName("전공영역--동아리-전공(심화), 취업동아리 참여 실적 ※학기별 최대 2개까지 인정").build(),
                    Category.builder().id(11L).name("MAJOR-OUT_SCHOOL-ATTENDANCE_OFFICIAL_CONTEST").weight(25f).koreanName("전공영역-교외 대회 및 교육 참가-공문을 통한 전공분야 대회").build(),
                    Category.builder().id(12L).name("MAJOR-OUT_SCHOOL-ATTENDANCE_UNOFFICIAL_CONTEST").weight(25f).koreanName("전공영역-교외 대회 및 교육 참가-전공 분야 대회 개별 참여").build(),
                    Category.builder().id(13L).name("MAJOR-OUT_SCHOOL-ATTENDANCE_HACKATHON").weight(25f).koreanName("전공영역-교외 대회 및 교육 참가-연합 해커톤").build(),
                    Category.builder().id(14L).name("MAJOR-OUT_SCHOOL-ATTENDANCE_SEMINAR").weight(25f).koreanName("전공영역-교외 대회 및 교육 참가-전공 관련 교육프로그램(특강, 연수, 세미나)").build(),
                    Category.builder().id(15L).name("MAJOR-IN_SCHOOL-ATTENDANCE_GSMFEST").weight(50f).koreanName("전공영역-교내 대회 및 교육 참가-GSM Festival").build(),
                    Category.builder().id(16L).name("MAJOR-IN_SCHOOL-ATTENDANCE_HACKATHON").weight(50f).koreanName("전공영역-교내 대회 및 교육 참가-교내해커톤").build(),
                    Category.builder().id(17L).name("MAJOR-IN_SCHOOL-ATTENDANCE_CLUB-PRESENTATION").weight(50f).koreanName("전공영역-교내 대회 및 교육 참가-전공동아리 발표대").build(),
                    Category.builder().id(18L).name("MAJOR-IN_SCHOOL-ATTENDANCE_SEMINAR").weight(15f).koreanName("전공영역-교내 대회 및 교육 참가-전공특강(방과후)").build(),
                    Category.builder().id(19L).name("MAJOR-IN_SCHOOL-ATTENDANCE_AFTER-SCHOOL").weight(45f).koreanName("전공영역-교내 대회 및 교육 참가-전공 관련 방과 후 학교 이수").build(),
                    Category.builder().id(20L).name("HUMANITIES-AWARD_CAREER-HUMANITY-IN_SCHOOL").weight(50f).koreanName("인문/인성 영역-수상경력-인성영역관련 수상-교내").build(),
                    Category.builder().id(21L).name("HUMANITIES-AWARD_CAREER-HUMANITY-OUT_SCHOOL").weight(50f).koreanName("인문/인성 영역-수상경력-인성영역관련 수상-교외").build(),
                    Category.builder().id(22L).name("HUMANITIES-READING-READ_A_THON-TURTLE").weight(40f).koreanName("인문/인성 영역-독서활동-빛고을독서마라톤-거북이코스").build(),
                    Category.builder().id(23L).name("HUMANITIES-READING-READ_A_THON-CROCODILE").weight(70f).koreanName("인문/인성 영역-독서활동-빛고을독서마라톤-악어코스").build(),
                    Category.builder().id(24L).name("HUMANITIES-READING-READ_A_THON-RABBIT_OVER").weight(100f).koreanName("인문/인성 영역-독서활동-빛고을독서마라톤-토끼코스 이").build(),
                    Category.builder().id(25L).name("HUMANITIES-READING").weight(10f).koreanName("인문/인성 영역-독서활동-전공서적 및 일반서적-학교지정도서 권장").build(),
                    Category.builder().id(26L).name("HUMANITIES-SERVICE-ACTIVITY").weight(5f).koreanName("인문/인성 영역-봉사-봉사 활동(교육과정의 봉사활동 제외)").build(),
                    Category.builder().id(27L).name("HUMANITIES-SERVICE-CLUB_SEMESTER_1").weight(50f).koreanName("인문/인성 영역-봉사-봉사동아지(에너지 지킴이 등)").build(),
                    Category.builder().id(28L).name("HUMANITIES-SERVICE-CLUB_SEMESTER_2").weight(50f).koreanName("인문/인성 영역-봉사-봉사동아지(에너지 지킴이 등)").build(),
                    Category.builder().id(29L).name("HUMANITIES-CERTIFICATE-CHINESE_CHARACTER").weight(50f).koreanName("인문/인성 영역-자격증 취득-한자 자격증-4급 이상").build(),
                    Category.builder().id(30L).name("HUMANITIES-CERTIFICATE-KOREAN_HISTORY").weight(50f).koreanName("인문/인성 영역-자격증 취득-한국사 자격증-3급 이상").build(),
                    Category.builder().id(31L).name("HUMANITIES-ACTIVITIES-NEWRROW_S").weight(5f).koreanName("인문/인성 영역-뉴로우S-참여성실도").build(),
                    Category.builder().id(32L).name("HUMANITIES-ACTIVITIES-SELF-DIRECTED_ACTIVITIES").weight(25f).koreanName("인문/인성 영역-활동영역-다양한 체육활동 및 문화예술 활동, 취업 준비를 위한 자기주도적 활동").build(),
                    Category.builder().id(33L).name("FOREIGN_LANG-ATTENDANCE-TOEIC_ACADMY_STATUS").weight(100f).koreanName("외국어 영역-토익 사관학교 참여-이수 여부").build(),
                    Category.builder().id(34L).name("FOREIGN_LANG-TOEIC_SCORE").weight(-1f).koreanName("외국어 영역-외국어 공인 시험-TOEIC").build(),
                    Category.builder().id(35L).name("FOREIGN_LANG-TOEFL_SCORE").weight(-1f).koreanName("외국어 영역-외국어 공인 시험-TOEFL").build(),
                    Category.builder().id(36L).name("FOREIGN_LANG-TEPS_SCORE").weight(-1f).koreanName("외국어 영역-외국어 공인 시험-TEPS").build(),
                    Category.builder().id(37L).name("FOREIGN_LANG-TOEIC_SPEAKING_LEVEL").weight(-1f).koreanName("외국어 영역-외국어 공인 시험-TOEIC Speaking").build(),
                    Category.builder().id(38L).name("FOREIGN_LANG-OPIC_SCORE").weight(-1f).koreanName("외국어 영역-외국어 공인 시험-OPIc").build(),
                    Category.builder().id(39L).name("FOREIGN_LANG-JPT_SCORE").weight(-1f).koreanName("외국어 영역-외국어 공인 시험-JPT").build(),
                    Category.builder().id(40L).name("FOREIGN_LANG-CPT_SCORE").weight(-1f).koreanName("외국어 영역-외국어 공인 시험-CPT").build(),
                    Category.builder().id(41L).name("FOREIGN_LANG-HSK_SCORE").weight(-1f).koreanName("외국어 영역-외국어 공인 시험-HSK").build()
            );
            Score score = Score.builder()
                    .category(categories.getFirst())
                    .member(member)
                    .value(1)
                    .build();
            when(studentDetailPersistencePort.findStudentDetailByGradeAndClassNumber(grade, classNumber))
                    .thenReturn(studentDetails);
            when(categoryPersistencePort.findAllCategory())
                    .thenReturn(categories);
            when(scorePersistencePort.findScoreByStudentDetailStudentCodes(List.of("24001")))
                    .thenReturn(List.of(score));

            // when
            MultipartFile result = getSheetService.execute(grade, classNumber);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getOriginalFilename()).isEqualTo("2-1-점수표.xlsx");
            assertThat(result.getContentType()).isEqualTo("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            assertThat(result.getSize()).isGreaterThan(0);
        }
    }
}