package team.incude.gsmc.v2.domain.member.application.usecase.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.incude.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.member.domain.StudentDetail;
import team.incude.gsmc.v2.domain.member.domain.StudentDetailWithEvidence;
import team.incude.gsmc.v2.domain.member.domain.constant.MemberRole;
import team.incude.gsmc.v2.domain.member.presentation.data.response.GetStudentResponse;
// import team.incude.gsmc.v2.domain.member.persistence.StudentDetailPersistenceAdapter;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("모든 학생 조회 서비스 클래스의")
class FindAllStudentServiceTest {

    @Mock
    private StudentDetailPersistencePort studentDetailPersistencePort;

    @InjectMocks
    private FindAllStudentService findAllStudentService;

    @Nested
    @DisplayName("execute() 메서드는")
    class Describe_getAllStudents {

        @Nested
        @DisplayName("학생 세부 정보가 EvidenceReviewStatus가 존재하는 경우에")
        class Context_with_existing_students {

            @Test
            @DisplayName("학생들의 정보를 반환한다")
            void it_returns_list_of_students() {
                // given
                Member member = Member.builder()
                        .email("student1@gsm.hs.kr")
                        .name("홍길동")
                        .role(MemberRole.ROLE_STUDENT)
                        .build();
                StudentDetail studentDetail = StudentDetail.builder()
                        .member(member)
                        .grade(2)
                        .classNumber(4)
                        .number(12)
                        .totalScore(87)
                        .build();
                StudentDetailWithEvidence dto = StudentDetailWithEvidence.builder()
                        .studentDetail(studentDetail)
                        .hasPendingEvidence(true)
                        .build();
                when(studentDetailPersistencePort.findStudentDetailWithEvidenceReviewStatusNotNullMember())
                        .thenReturn(List.of(dto));

                // when
                List<GetStudentResponse> responses = findAllStudentService.execute();

                // then
                assertThat(responses).hasSize(1);
                GetStudentResponse response = responses.getFirst();

                assertThat(response.email()).isEqualTo("student1@gsm.hs.kr");
                assertThat(response.name()).isEqualTo("홍길동");
                assertThat(response.grade()).isEqualTo(2);
                assertThat(response.classNumber()).isEqualTo(4);
                assertThat(response.number()).isEqualTo(12);
                assertThat(response.totalScore()).isEqualTo(87);
                assertThat(response.hasPendingEvidence()).isTrue();
                assertThat(response.role()).isEqualTo(MemberRole.ROLE_STUDENT);
            }
        }

        @Nested
        @DisplayName("EvidenceReviewStatus가 존재하는 학생이 아무도 없는 경우에")
        class Context_with_no_students {

            @Test
            @DisplayName("빈 리스트를 반환한다")
            void it_returns_empty_list() {
                // given
                when(studentDetailPersistencePort.findStudentDetailWithEvidenceReviewStatusNotNullMember())
                        .thenReturn(List.of());

                // when
                List<GetStudentResponse> responses = findAllStudentService.execute();

                // then
                assertThat(responses).isEmpty();
            }
        }
    }
}