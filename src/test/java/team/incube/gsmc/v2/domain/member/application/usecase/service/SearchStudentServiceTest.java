package team.incube.gsmc.v2.domain.member.application.usecase.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import team.incube.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incube.gsmc.v2.domain.member.domain.Member;
import team.incube.gsmc.v2.domain.member.domain.StudentDetail;
import team.incube.gsmc.v2.domain.member.domain.StudentDetailWithEvidence;
import team.incube.gsmc.v2.domain.member.domain.constant.MemberRole;
import team.incube.gsmc.v2.domain.member.presentation.data.response.SearchStudentResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("학생 검색 서비스 클래스의")
class SearchStudentServiceTest {

    @Mock
    private StudentDetailPersistencePort studentDetailPersistencePort;

    @InjectMocks
    private SearchStudentService searchStudentService;

    @Nested
    @DisplayName("execute(name, grade, classNumber, page, size) 메서드는")
    class Describe_execute {

        @Nested
        @DisplayName("검색 결과가 존재할 때")
        class Context_with_matching_students {

            @Test
            @DisplayName("학생 목록과 총 페이지, 총 요소 수를 반환한다")
            void it_returns_students_with_pagination_info() {
                // given
                String name = "홍길동";
                Integer grade = 2;
                Integer classNumber = 1;
                Integer page = 0;
                Integer size = 10;
                Member member = Member.builder()
                        .email("hong@gsm.hs.kr")
                        .name(name)
                        .role(MemberRole.ROLE_STUDENT)
                        .build();
                StudentDetail studentDetail = StudentDetail.builder()
                        .member(member)
                        .grade(grade)
                        .classNumber(classNumber)
                        .number(5)
                        .totalScore(90)
                        .build();
                StudentDetailWithEvidence studentDetailWithEvidence = StudentDetailWithEvidence.builder()
                        .studentDetail(studentDetail)
                        .hasPendingEvidence(true)
                        .build();
                Page<StudentDetailWithEvidence> studentPage = new PageImpl<>(
                        List.of(studentDetailWithEvidence),
                        PageRequest.of(page, size),
                        1
                );
                when(studentDetailPersistencePort.searchStudentDetailWithEvidenceReviewStatusNotNullMember(
                        name, grade, classNumber, null, PageRequest.of(page, size)
                )).thenReturn(studentPage);

                // when
                SearchStudentResponse response = searchStudentService.execute(name, grade, classNumber, null, page, size);

                // then
                assertThat(response.totalPage()).isEqualTo(1);
                assertThat(response.totalElements()).isEqualTo(1);
                assertThat(response.results()).hasSize(1);
                assertThat(response.results().getFirst().email()).isEqualTo("hong@gsm.hs.kr");
                assertThat(response.results().getFirst().name()).isEqualTo("홍길동");
                assertThat(response.results().getFirst().grade()).isEqualTo(2);
                assertThat(response.results().getFirst().classNumber()).isEqualTo(1);
                assertThat(response.results().getFirst().number()).isEqualTo(5);
                assertThat(response.results().getFirst().totalScore()).isEqualTo(90);
                assertThat(response.results().getFirst().hasPendingEvidence()).isTrue();
                assertThat(response.results().getFirst().role()).isEqualTo(MemberRole.ROLE_STUDENT);
            }
        }

        @Nested
        @DisplayName("검색 결과가 없을 때")
        class Context_with_no_matching_students {

            @Test
            @DisplayName("빈 학생 목록과 총 페이지 0, 총 요소 수 0을 반환한다")
            void it_returns_empty_result() {
                // given
                String name = "없는사람";
                Integer grade = 3;
                Integer classNumber = 2;
                Integer page = 0;
                Integer size = 10;
                Page<StudentDetailWithEvidence> emptyPage = new PageImpl<>(
                        List.of(),
                        PageRequest.of(page, size),
                        0
                );
                when(studentDetailPersistencePort.searchStudentDetailWithEvidenceReviewStatusNotNullMember(
                        name, grade, classNumber, null, PageRequest.of(page, size)
                )).thenReturn(emptyPage);

                // when
                SearchStudentResponse response = searchStudentService.execute(name, grade, classNumber, null, page, size);

                // then
                assertThat(response.totalPage()).isEqualTo(0);
                assertThat(response.totalElements()).isEqualTo(0);
                assertThat(response.results()).isEmpty();
            }
        }

        @Nested
        @DisplayName("정렬 조건이 있을 때")
        class Context_with_sort_direction {

            @Test
            @DisplayName("총점 내림차순으로 정렬된 학생 목록을 반환한다")
            void it_returns_students_sorted_by_total_score_desc() {
                // given
                String name = null;
                Integer grade = null;
                Integer classNumber = null;
                team.incube.gsmc.v2.domain.member.domain.constant.MemberSortDirection sortBy = team.incube.gsmc.v2.domain.member.domain.constant.MemberSortDirection.TOTAL_SCORE_DESC;
                Integer page = 0;
                Integer size = 10;
                Member member1 = Member.builder()
                        .email("student1@gsm.hs.kr")
                        .name("학생1")
                        .role(MemberRole.ROLE_STUDENT)
                        .build();
                Member member2 = Member.builder()
                        .email("student2@gsm.hs.kr")
                        .name("학생2")
                        .role(MemberRole.ROLE_STUDENT)
                        .build();
                StudentDetail studentDetail1 = StudentDetail.builder()
                        .member(member1)
                        .grade(2)
                        .classNumber(1)
                        .number(1)
                        .totalScore(85)
                        .build();
                StudentDetail studentDetail2 = StudentDetail.builder()
                        .member(member2)
                        .grade(2)
                        .classNumber(1)
                        .number(2)
                        .totalScore(95)
                        .build();
                StudentDetailWithEvidence evidence1 = StudentDetailWithEvidence.builder()
                        .studentDetail(studentDetail1)
                        .hasPendingEvidence(false)
                        .build();

                StudentDetailWithEvidence evidence2 = StudentDetailWithEvidence.builder()
                        .studentDetail(studentDetail2)
                        .hasPendingEvidence(true)
                        .build();
                Page<StudentDetailWithEvidence> studentPage = new PageImpl<>(
                        List.of(evidence2, evidence1),
                        PageRequest.of(page, size),
                        2
                );
                when(studentDetailPersistencePort.searchStudentDetailWithEvidenceReviewStatusNotNullMember(
                        name, grade, classNumber, sortBy, PageRequest.of(page, size)
                )).thenReturn(studentPage);

                // when
                SearchStudentResponse response = searchStudentService.execute(name, grade, classNumber, sortBy, page, size);

                // then
                assertThat(response.totalPage()).isEqualTo(1);
                assertThat(response.totalElements()).isEqualTo(2);
                assertThat(response.results()).hasSize(2);
                assertThat(response.results().get(0).totalScore()).isEqualTo(95); // 첫 번째가 더 높은 점수
                assertThat(response.results().get(1).totalScore()).isEqualTo(85); // 두 번째가 더 낮은 점수
            }
        }
    }
}