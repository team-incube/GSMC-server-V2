package team.incude.gsmc.v2.domain.member.application.port;

import team.incude.gsmc.v2.domain.member.domain.StudentDetail;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.util.List;

@Port(direction = PortDirection.OUTBOUND)
public interface StudentDetailPersistencePort {
    StudentDetail findStudentDetailByStudentCode(String studentCode);

    StudentDetail findStudentDetailByMemberEmail(String email);

    List<StudentDetail> findStudentDetailByGradeAndClassNumber(Integer grade, Integer classNumber);

    // Member가 null이 아닌 student detail 조회
    List<StudentDetail> findStudentDetailNotNullMember();

    Integer findTotalScoreByMemberEmail(String email);

    Integer findTotalScoreByStudentCode(String studentCode);

    StudentDetail saveStudentDetail(StudentDetail studentDetail);
}