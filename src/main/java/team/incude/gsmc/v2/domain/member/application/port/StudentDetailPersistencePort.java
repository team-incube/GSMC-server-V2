package team.incude.gsmc.v2.domain.member.application.port;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team.incude.gsmc.v2.domain.member.domain.StudentDetail;
import team.incude.gsmc.v2.domain.member.domain.StudentDetailWithEvidence;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.util.List;

@Port(direction = PortDirection.OUTBOUND)
public interface StudentDetailPersistencePort {
    StudentDetail findStudentDetailByStudentCode(String studentCode);

    StudentDetail findStudentDetailByMemberEmail(String email);

    List<StudentDetail> findStudentDetailByGradeAndClassNumber(Integer grade, Integer classNumber);

    StudentDetailWithEvidence findStudentDetailWithEvidenceByStudentCode(String studentCode);

    StudentDetailWithEvidence findStudentDetailWithEvidenceByMemberEmail(String email);

    List<StudentDetailWithEvidence> findStudentDetailWithEvidenceReviewStatusNotNullMember();

    Page<StudentDetailWithEvidence> searchStudentDetailWithEvidenceReiewStatusNotNullMember(String name, Integer grade, Integer classNumber, Pageable pageable);

    Integer findTotalScoreByMemberEmail(String email);

    Integer findTotalScoreByStudentCode(String studentCode);

    StudentDetail saveStudentDetail(StudentDetail studentDetail);
}