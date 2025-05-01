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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("특정 학생 조회 서비스 클래스의")
class FindStudentByStudentCodeServiceTest {

    @Mock
    private StudentDetailPersistencePort studentDetailPersistencePort;

    @InjectMocks
    private FindStudentByStudentCodeService findStudentByStudentCodeService;

    @Nested
    @DisplayName("execute(studentCode) 메서드는")
    class Describe_execute {

        @Nested
        @DisplayName("유효한 studentCode가 주어졌을 때")
        class Context_with_valid_studentCode {

            @Test
            @DisplayName("해당 학생의 상세 정보를 반환한다")
            void it_returns_student_detail() {
                // given
                String studentCode = "24058";
                Member member = Member.builder()
                        .email("student@gsm.hs.kr")
                        .name("홍길동")
                        .role(MemberRole.ROLE_STUDENT)
                        .build();
                StudentDetail studentDetail = StudentDetail.builder()
                        .member(member)
                        .grade(2)
                        .classNumber(3)
                        .number(15)
                        .totalScore(92)
                        .build();
                StudentDetailWithEvidence studentDetailWithEvidence = StudentDetailWithEvidence.builder()
                        .studentDetail(studentDetail)
                        .hasPendingEvidence(false)
                        .build();
                when(studentDetailPersistencePort.findStudentDetailWithEvidenceByStudentCode(studentCode))
                        .thenReturn(studentDetailWithEvidence);

                // when
                GetStudentResponse response = findStudentByStudentCodeService.execute(studentCode);

                // then
                assertThat(response.email()).isEqualTo("student@gsm.hs.kr");
                assertThat(response.name()).isEqualTo("홍길동");
                assertThat(response.grade()).isEqualTo(2);
                assertThat(response.classNumber()).isEqualTo(3);
                assertThat(response.number()).isEqualTo(15);
                assertThat(response.totalScore()).isEqualTo(92);
                assertThat(response.hasPendingEvidence()).isFalse();
                assertThat(response.role()).isEqualTo(MemberRole.ROLE_STUDENT);
            }
        }
    }
}