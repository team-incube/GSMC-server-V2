package team.incube.gsmc.v2.domain.member.application.usecase.service;

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
import team.incube.gsmc.v2.domain.member.domain.StudentDetailWithEvidence;
import team.incube.gsmc.v2.domain.member.domain.constant.MemberRole;
import team.incube.gsmc.v2.domain.member.presentation.data.response.GetStudentResponse;
import team.incube.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("현재 로그인된 학생 조회 서비스 클래스의")
class FindCurrentStudentServiceTest {

    @Mock
    private StudentDetailPersistencePort studentDetailPersistencePort;

    @Mock
    private CurrentMemberProvider currentMemberProvider;

    @InjectMocks
    private FindCurrentStudentService findCurrentStudentService;

    @Nested
    @DisplayName("execute() 메서드는")
    class Describe_execute {

        @Nested
        @DisplayName("현재 로그인된 사용자가 존재할 때")
        class Context_with_authenticated_user {

            @Test
            @DisplayName("학생의 상세 정보를 반환한다")
            void it_returns_current_student_detail() {
                // given
                String email = "student@gsm.hs.kr";
                Member member = Member.builder()
                        .email(email)
                        .name("홍길동")
                        .role(MemberRole.ROLE_STUDENT)
                        .build();
                StudentDetail studentDetail = StudentDetail.builder()
                        .member(member)
                        .grade(2)
                        .classNumber(4)
                        .number(12)
                        .totalScore(95)
                        .build();
                StudentDetailWithEvidence studentDetailWithEvidence = StudentDetailWithEvidence.builder()
                        .studentDetail(studentDetail)
                        .hasPendingEvidence(true)
                        .build();

                when(currentMemberProvider.getCurrentUser()).thenReturn(member);
                when(studentDetailPersistencePort.findStudentDetailWithEvidenceByMemberEmail(email))
                        .thenReturn(studentDetailWithEvidence);

                // when
                GetStudentResponse response = findCurrentStudentService.execute();

                // then
                assertThat(response.email()).isEqualTo(email);
                assertThat(response.name()).isEqualTo("홍길동");
                assertThat(response.grade()).isEqualTo(2);
                assertThat(response.classNumber()).isEqualTo(4);
                assertThat(response.number()).isEqualTo(12);
                assertThat(response.totalScore()).isEqualTo(95);
                assertThat(response.hasPendingEvidence()).isTrue();
                assertThat(response.role()).isEqualTo(MemberRole.ROLE_STUDENT);
            }
        }
    }
}